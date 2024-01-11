package com.adamsky.Database;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_tokens")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token")
    private String token;

    public UserToken() { }

    public UserToken(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
