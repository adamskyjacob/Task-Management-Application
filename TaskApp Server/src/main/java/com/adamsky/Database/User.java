package com.adamsky.Database;

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
    @Column(name="id")
    public long id;

    @Column(name="username")
    public String username;

    @Column(name="email", nullable = true)
    public String email;

    @Column(name="password")
    public String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskUser> taskUsers = new ArrayList<>();

    public void addTaskUser(TaskUser taskUser) {
        taskUsers.add(taskUser);
        taskUser.setUser(this);
    }

    public User(){}
    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
