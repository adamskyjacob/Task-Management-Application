package com.adamsky.WebSocket.Task;

import lombok.Data;

@Data
public class NewTask {
    private Long owner;
    private String description;
    private String deadline;
    private Long[] users;

    public NewTask(Long owner, String description, String deadline, Long[] users){
        this.owner = owner;
        this.description = description;
        this.deadline = deadline;
        this.users = users;
    }
}
