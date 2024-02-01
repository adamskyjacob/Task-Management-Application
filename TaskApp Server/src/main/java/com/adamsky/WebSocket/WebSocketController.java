package com.adamsky.WebSocket;

import com.adamsky.Database.Task.Task;
import com.adamsky.Database.Task.TaskRepository;
import com.adamsky.Database.TaskUser.TaskUserRepository;
import com.adamsky.Database.User.User;
import com.adamsky.Database.User.UserRepository;
import com.adamsky.Database.User.UserToken;
import com.adamsky.Database.User.UserTokenRepository;
import com.adamsky.WebSocket.Activity.ActivityRequest;
import com.adamsky.WebSocket.Activity.ActivityType;
import com.adamsky.WebSocket.Task.CreateTaskRequest;
import com.adamsky.WebSocket.Task.NewTask;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static com.adamsky.TaskApplication.print;

@Controller
@CrossOrigin(origins = {"https://localhost:3000"})
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private TaskUserRepository taskUserRepository;
    @Autowired
    private TaskRepository taskRepository;

    private final SimpMessageSendingOperations messagingTemplate;

    public WebSocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/getTasks")
    public void getTasks(@Payload Long id) {
        List<Task> tasks = taskRepository.findAllByOwnerId(id);
        messagingTemplate.convertAndSend("/topic/taskList", tasks);
    }

    @MessageMapping("/toggleActive")
    public void toggleActive(@RequestBody ActivityRequest request){
        Optional<User> optUser = userRepository.findById(request.getUserId());
        if(optUser.isEmpty()){
            return;
        }

        User user = optUser.get();
        user.setActive(request.getType().equals(ActivityType.LOGIN));
        userRepository.save(user);
    }

    @MessageMapping("/createTask")
    public void createTask(@RequestBody CreateTaskRequest request) {
        Optional<UserToken> userToken;
        if((userToken = userTokenRepository.findByUserId(request.getId())).isEmpty()){
            return;
        }

        if(!Objects.equals(userToken.get().getToken(), request.getToken())) {
            return;
        }

        NewTask newTask = request.getNewTask();
        print(newTask.toString());
        Task task = new Task();
        try {
            task.setOwner(userRepository.findById(newTask.getOwner()).orElseThrow(EntityNotFoundException::new));
            task.setTaskUsers(Arrays.stream(newTask.getUsers()).map(uid -> taskUserRepository.findById(uid).orElseThrow(EntityNotFoundException::new)).collect(Collectors.toList()));
            task.setSubtasks(new ArrayList<>());
            task.setDeadline(Date.valueOf(newTask.getDeadline()));
            taskRepository.save(task);
        } catch(EntityNotFoundException e){
            print("Couldn't find User with ID '" + newTask.getOwner() + "' in UserRepository.");
        }
        messagingTemplate.convertAndSend("/topic/taskCreated", task.getId());
    }
}