package com.adamsky.Database.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByTaskUsersUserUsername(String username);
    List<Task> findAllByOwnerId(Long id);
}