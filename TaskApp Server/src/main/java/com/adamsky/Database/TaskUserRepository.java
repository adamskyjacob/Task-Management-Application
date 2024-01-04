package com.adamsky.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskUserRepository extends JpaRepository<TaskUser, Long> {
}