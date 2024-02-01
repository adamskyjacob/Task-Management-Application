package com.adamsky.Database.Task;

import com.adamsky.Database.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.sql.Date;

@Data
@MappedSuperclass
public abstract class STask {
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private Date deadline;
}
