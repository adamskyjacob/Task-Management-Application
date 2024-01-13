package com.adamsky.RestAPI;

import com.adamsky.Database.Task.Task;
import com.adamsky.Database.Task.TaskRepository;
import com.adamsky.Database.Task.TaskRequest;
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
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CredentialController {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskUserRepository taskUserRepository;
    private final UserTokenRepository userTokenRepository;

    @Autowired
    public CredentialController(UserRepository userRepository, TaskRepository taskRepository, TaskUserRepository taskUserRepository, UserTokenRepository userTokenRepository){
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskUserRepository = taskUserRepository;
        this.userTokenRepository = userTokenRepository;
    }

    private void registerUser(User user){
        userRepository.save(user);

        TaskUser taskUser = new TaskUser(null, null, user);
        taskUserRepository.save(taskUser);
    }

    @GetMapping(value = "/get_tasks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Task>> getTasksForUser(@RequestParam String username){
        List<Task> tasks = taskRepository.findAllByTaskUsersUserUsername(username);
        HttpStatus status;
        if(tasks.isEmpty()){
            status = HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(tasks);
        }

        status = HttpStatus.ACCEPTED;
        return ResponseEntity.status(status).body(tasks);
    }

    @PostMapping(value = "/validate_token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenValidation> postValidateToken(@RequestBody TokenValidationRequest request) {
        HttpStatus status;
        System.out.println(request.getIdentifier());
        Optional<User> user = userRepository.findByUsername(request.getIdentifier());
        if(user.isEmpty()){
            status = HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(new TokenValidation(false));
        }

        Optional<UserToken> userToken = userTokenRepository.findById(user.get().getId());
        if(userToken.isEmpty()){
            status = HttpStatus.FORBIDDEN;
            return ResponseEntity.status(status).body(new TokenValidation(false));
        }

        if(userToken.get().getToken().equals(request.getToken())){
            status = HttpStatus.ACCEPTED;
            return ResponseEntity.status(status).body(new TokenValidation(true));
        }

        status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new TokenValidation(false));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CredentialResponse> postRegister(@RequestBody RegisterRequest request) {
        User user;
        HttpStatus status;
        if(request.isEmail){
            String username = request.identifier.split("@")[0];
            if(userRepository.findByUsername(username).isPresent()){
                status = HttpStatus.CONFLICT;
                return ResponseEntity.status(status).body(RegisterError.from(status));
            }
            user = new User(username, request.identifier, passwordEncoder.encode(request.password));
            registerUser(user);
        } else {
            if(userRepository.findByEmail(request.identifier).isPresent()){
                status = HttpStatus.CONFLICT;
                return ResponseEntity.status(status).body(RegisterError.from(status));
            }

            user = new User(request.identifier, null, passwordEncoder.encode(request.password));
            registerUser(user);
        }

        Optional<UserToken> userToken = userTokenRepository.findByUserUsername(user.getUsername());
        if(userToken.isEmpty()){
            try {
                userToken = Optional.of(new UserToken(user, generateToken(user.getUsername())));
                userTokenRepository.save(userToken.get());
            } catch (NoSuchAlgorithmException | IOException e) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                return ResponseEntity.status(status).body(RegisterError.from(status));
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterSuccess(userToken.get().getToken()));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CredentialResponse> postLogin(@RequestBody LoginRequest request) {
        HttpStatus status;
        Optional<User> optionalUser = request.isEmail ? userRepository.findByEmail(request.identifier) : userRepository.findByUsername(request.identifier);
        if(optionalUser.isEmpty()){
            status = HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(LoginError.from(status));
        }

        User user = optionalUser.get();
        Optional<UserToken> userToken = userTokenRepository.findByUserUsername(user.getUsername());

        if(!passwordEncoder.matches(request.password, user.getPassword())){
            status = HttpStatus.FORBIDDEN;
            return ResponseEntity.status(status).body(LoginError.from(status));
        }

        if(userToken.isEmpty()) {
            try {
                userToken = Optional.of(new UserToken(user, generateToken(user.getUsername())));
                userTokenRepository.save(userToken.get());
            } catch (NoSuchAlgorithmException | IOException e) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                return ResponseEntity.status(status).body(LoginError.from(status));
            }
        }

        CredentialResponse response = new LoginSuccess(userToken.get().getToken());
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