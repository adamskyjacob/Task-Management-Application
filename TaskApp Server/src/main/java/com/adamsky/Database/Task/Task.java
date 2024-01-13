package com.adamsky.Database.Task;

import com.adamsky.Database.TaskUser.TaskUser;
import com.adamsky.Database.User.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tasks")
public class Task extends STask {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Subtask> subtasks = new ArrayList<>();

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskUser> taskUsers = new ArrayList<>();

	public void addSubtask(Subtask subtask) {
		subtasks.add(subtask);
		subtask.setTask(this);
	}

	public void addUser(User user) {
		TaskUser taskUser = new TaskUser(this, null, user);
		taskUsers.add(taskUser);
		user.getTaskUsers().add(taskUser);
	}

	public Task(){}
}
