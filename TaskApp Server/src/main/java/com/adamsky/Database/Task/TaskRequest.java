package com.adamsky.Database.Task;

import lombok.Data;

import java.util.Optional;

@Data
public class TaskRequest {
    String username;
    Long taskId;
}
