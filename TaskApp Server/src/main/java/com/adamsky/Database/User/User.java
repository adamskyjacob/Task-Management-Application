package com.adamsky.Database.User;

import com.adamsky.Database.TaskUser.TaskUser;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="username")
    private String username;

    @Column(name="email", nullable = true)
    private String email;

    @Column(name="password")
    private String password;

    @OneToOne(mappedBy = "user")
    private UserToken userToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskUser> taskUsers = new ArrayList<>();

    public void addTaskUser(TaskUser taskUser) {
        taskUsers.add(taskUser);
        taskUser.setUser(this);
    }

    public User() { }
    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
