package com.adamsky.RestAPI.Credential;

import com.adamsky.Database.TaskUser.TaskUser;
import com.adamsky.Database.TaskUser.TaskUserRepository;
import com.adamsky.Database.User.User;
import com.adamsky.Database.User.UserRepository;
import com.adamsky.Database.User.UserToken;
import com.adamsky.Database.User.UserTokenRepository;
import com.adamsky.RestAPI.Login.LoginError;
import com.adamsky.RestAPI.Login.LoginRequest;
import com.adamsky.RestAPI.Login.LoginSuccess;
import com.adamsky.RestAPI.Register.RegisterError;
import com.adamsky.RestAPI.Register.RegisterRequest;
import com.adamsky.RestAPI.Register.RegisterSuccess;
import com.adamsky.RestAPI.TokenValidation.TokenValidation;
import com.adamsky.RestAPI.TokenValidation.TokenValidationRequest;

import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

import static com.adamsky.TaskApplication.logger;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CredentialController {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final TaskUserRepository taskUserRepository;
    private final UserTokenRepository userTokenRepository;

    @Autowired
    public CredentialController(UserRepository userRepository, TaskUserRepository taskUserRepository, UserTokenRepository userTokenRepository){
        this.userRepository = userRepository;
        this.taskUserRepository = taskUserRepository;
        this.userTokenRepository = userTokenRepository;
    }

    private void registerUser(User user){
        this.userRepository.save(user);

        TaskUser taskUser = new TaskUser(null, null, user);
        this.taskUserRepository.save(taskUser);
    }

    @PostMapping(value = "/validateToken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenValidation> postValidateToken(@RequestBody TokenValidationRequest request) {
        HttpStatus status;
        Optional<User> user = this.userRepository.findByUsername(request.getIdentifier());
        if(user.isEmpty()){
            logger.warn("No user provided for token validation request.");
            status = HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(new TokenValidation(false));
        }

        Optional<UserToken> userToken = this.userTokenRepository.findByUserId(user.get().getId());
        if(userToken.isEmpty()){
            logger.warn("Couldn't find user token for user.");
            status = HttpStatus.FORBIDDEN;
            return ResponseEntity.status(status).body(new TokenValidation(false));
        }

        if(userToken.get().getToken().equals(request.getToken())){
            logger.info(String.format("Token validation success for UID %d.", user.get().getId()));
            status = HttpStatus.ACCEPTED;
            return ResponseEntity.status(status).body(new TokenValidation(true));
        }

        logger.warn("Not sure what happened here!");
        status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new TokenValidation(false));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CredentialResponse> postRegister(@RequestBody RegisterRequest request) {
        User user;
        HttpStatus status;
        if(request.getIsEmail()){
            if(this.userRepository.findByEmail(request.getIdentifier()).isPresent()){
                logger.info(String.format("Duplicate user registration attempt with email %s.", request.getIdentifier()));
                status = HttpStatus.CONFLICT;
                return ResponseEntity.status(status).body(RegisterError.from(status));
            }

            user = new User(request.getIdentifier(), null, this.passwordEncoder.encode(request.getPassword()));
            registerUser(user);
            logger.info(String.format("Registered user using username with UID %d.", user.getId()));
        } else {
            if(this.userRepository.findByUsername(request.getIdentifier()).isPresent()){
                logger.info(String.format("Duplicate user registration attempt for username %s.", request.getIdentifier()));
                status = HttpStatus.CONFLICT;
                return ResponseEntity.status(status).body(RegisterError.from(status));
            }

            user = new User(request.getIdentifier(), request.getIdentifier(), this.passwordEncoder.encode(request.getPassword()));
            registerUser(user);
            logger.info(String.format("Registered user using email with UID %d.", user.getId()));
        }

        Optional<UserToken> userToken = this.userTokenRepository.findByUserUsername(user.getUsername());
        if(userToken.isEmpty()){
            try {
                userToken = Optional.of(new UserToken(user, generateToken(user.getUsername())));
                this.userTokenRepository.save(userToken.get());
            } catch (NoSuchAlgorithmException | IOException e) {
                logger.warn(String.format("generateToken failed for UID %d.", user.getId()));
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                return ResponseEntity.status(status).body(RegisterError.from(status));
            }
        }

        CredentialResponse response = new RegisterSuccess(userToken.get().getToken(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CredentialResponse> postLogin(@RequestBody LoginRequest request) {
        HttpStatus status;
        Optional<User> optionalUser = request.getIsEmail() ? this.userRepository.findByEmail(request.getIdentifier()) : this.userRepository.findByUsername(request.getIdentifier());
        if(optionalUser.isEmpty()){
            logger.warn(String.format("Failed to find user with the %s %s", request.getIsEmail() ? "email" : "username", request.getIdentifier()));
            status = HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(LoginError.from(status));
        }

        User user = optionalUser.get();
        Optional<UserToken> userToken = userTokenRepository.findByUserUsername(user.getUsername());

        if(!this.passwordEncoder.matches(request.getPassword(), user.getPassword())){
            logger.warn(String.format("Failed to find user with the %s %s", request.getIsEmail() ? "email" : "username", request.getIdentifier()));
            status = HttpStatus.FORBIDDEN;
            return ResponseEntity.status(status).body(LoginError.from(status));
        }

        if(userToken.isEmpty()) {
            try {
                userToken = Optional.of(new UserToken(user, generateToken(user.getUsername())));
                this.userTokenRepository.save(userToken.get());
            } catch (NoSuchAlgorithmException | IOException e) {
                logger.warn(String.format("generateToken failed for UID %d.", user.getId()));
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                return ResponseEntity.status(status).body(LoginError.from(status));
            }
        }

        CredentialResponse response = new LoginSuccess(userToken.get().getToken(), user.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }


    public static String generateToken(String username) throws NoSuchAlgorithmException, IOException {
        SecretKey secretKey = getSecretKey();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 2592000000L);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    private static SecretKey getSecretKey() throws NoSuchAlgorithmException, IOException {
        byte[] keyBytes = readSecretKeyFromFile();
        if (keyBytes != null) {
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        }

        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();

        saveSecretKeyToFile(secretKey.getEncoded());
        return secretKey;
    }

    private static byte[] readSecretKeyFromFile() throws IOException {
        Resource resource = new ClassPathResource("secret-key.key");
        if (resource.exists()) {
            return Files.readAllBytes(Path.of(resource.getURI()));
        }
        return null;
    }

    private static void saveSecretKeyToFile(byte[] keyBytes) throws IOException {
        Files.write(Path.of("secret-key.key"), keyBytes, StandardOpenOption.CREATE);
    }
}