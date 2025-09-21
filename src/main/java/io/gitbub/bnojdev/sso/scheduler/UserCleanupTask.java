package io.github.bnojdev.sso;

import lombok.extern.slf4j.Slf4j;
import io.github.bnojdev.sso.model.User;
import io.github.bnojdev.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class UserCleanupTask {
    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 10000)
    public void removeUnactivatedUsers() {
        log.info("Running scheduled task to remove unactivated users.");
        userService.removeUnactivatedUsers();
    }
}
