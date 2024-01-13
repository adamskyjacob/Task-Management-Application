package com.adamsky.Database;

import com.adamsky.Database.User.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private final UserTokenRepository userTokenRepository;

    @Autowired
    public Scheduler(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void clearUserTokens() {
        userTokenRepository.deleteAll();
    }

}
