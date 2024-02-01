package com.adamsky.Database.TaskUser;

import com.adamsky.Database.Task.Subtask;
import com.adamsky.Database.Task.Task;
import com.adamsky.Database.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
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
