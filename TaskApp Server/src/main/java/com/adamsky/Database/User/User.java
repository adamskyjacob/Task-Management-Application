package com.adamsky.Database.User;

import com.adamsky.Database.Task.Subtask;
import com.adamsky.Database.TaskUser.TaskUser;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="username")
    private String username;

    @JsonIgnore
    @Column(name="email")
    private String email;

    @JsonIgnore
    @Column(name="password")
    private String password;

    @Column(name="active")
    private Boolean active = false;

    @OneToOne(mappedBy = "user")
    private UserToken userToken;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TaskUser> taskUsers = new ArrayList<>();

    public User(String username, String email, String password) {
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
    }

    @JsonProperty("taskUserIds")
    public List<Long> getTaskUserIds() {
        return taskUsers.stream()
                .map(TaskUser::getId)
                .collect(Collectors.toList());
    }
    public void addTaskUser(TaskUser taskUser) {
        taskUsers.add(taskUser);
        taskUser.setUser(this);
    }

    public User(){}
}
