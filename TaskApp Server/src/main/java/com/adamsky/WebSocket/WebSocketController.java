package com.adamsky.WebSocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import com.adamsky.Database.Task.Task;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;

    public WebSocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/createTask")
    public void createTask(@Payload Task task) {
    	System.out.println(task.description);
        messagingTemplate.convertAndSend("/topic/taskCreated", "This is a sample message");
    }
}