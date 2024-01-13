package com.adamsky.Database.Task;

import com.adamsky.Database.TaskUser.TaskUser;
import com.adamsky.Database.User.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "subtasks")
public class Subtask extends STask{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @OneToMany(mappedBy = "subtask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskUser> taskUsers = new ArrayList<>();

    public void addUser(User user) {
        TaskUser taskUser = new TaskUser(null, this, user);
        taskUsers.add(taskUser);
        user.getTaskUsers().add(taskUser);
    }

    public Subtask(){}
}
