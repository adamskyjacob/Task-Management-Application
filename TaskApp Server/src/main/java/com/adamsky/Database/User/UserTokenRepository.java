package com.adamsky.Database.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserUsername(String username);
    Optional<UserToken> findByUserId(Long id);
}
