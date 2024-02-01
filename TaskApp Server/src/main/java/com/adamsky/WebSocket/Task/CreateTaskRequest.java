package com.adamsky.WebSocket.Task;

import lombok.Data;

@Data
public class CreateTaskRequest {
    private NewTask newTask;
    private String token;
    private Long id;
    private CreateTaskRequest(NewTask newTask, String token, Long id) {
        this.newTask = newTask;
        this.token = token;
        this.id = id;
    }
}
