package com.adamsky.RestAPI;

import com.adamsky.Database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.adamsky.TaskApplication.print;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CredentialController {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskUserRepository taskUserRepository;

    @Autowired
    public CredentialController(UserRepository userRepository, TaskRepository taskRepository, TaskUserRepository taskUserRepository){
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskUserRepository = taskUserRepository;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postRegister(@RequestBody RegisterRequest request) {
        print(request.toString());
        if(request.isEmail){
            String username = request.identifier.split("@")[0];
            if(userRepository.findByUsername(username).isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            }

            User user = new User(username, request.identifier, passwordEncoder.encode(request.password));
            userRepository.save(user);

            TaskUser taskUser = new TaskUser(null, null, user);
            taskUserRepository.save(taskUser);
        } else {
            if(userRepository.findByEmail(request.identifier).isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            }

            User user = new User(request.identifier, null, passwordEncoder.encode(request.password));
            userRepository.save(user);

            TaskUser taskUser = new TaskUser(null, null, user);
            taskUserRepository.save(taskUser);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
    }
    @PostMapping("/login")
    public ResponseEntity<String> postLogin(@RequestBody LoginRequest request) {
        print(request.toString());
        Optional<User> user = request.isEmail ? userRepository.findByEmail(request.identifier) : userRepository.findByUsername(request.identifier);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if(passwordEncoder.matches(request.password, user.get().getPassword())){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Login successful.");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect password.");
    }
}