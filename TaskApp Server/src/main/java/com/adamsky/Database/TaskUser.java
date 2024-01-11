package com.adamsky.Database;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "task_users")
public class TaskUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "subtask_id")
    private Subtask subtask;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    public TaskUser(Task task, Subtask subtask, User user){
        this.task = task;
        this.subtask = subtask;
        this.user = user;
    }

    public TaskUser() { }
}
