package com.adamsky.Database.Task;

import lombok.Data;

@Data
public class TaskRequest {
    private String username;
    private Long taskId;

    public TaskRequest(String username, Long taskId){
        this.username = username;
        this.taskId = taskId;
    }
}
