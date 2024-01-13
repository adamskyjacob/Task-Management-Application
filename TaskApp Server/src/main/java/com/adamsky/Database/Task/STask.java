package com.adamsky.Database.Task;

import com.adamsky.Database.User.User;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class STask {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    public User owner;

    @Column(name = "description")
    public String description;
}
