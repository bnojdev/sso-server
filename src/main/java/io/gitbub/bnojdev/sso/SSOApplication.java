package io.github.bnojdev.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SSOApplication {
    public static void main(String[] args) {
        SpringApplication.run(SSOApplication.class, args);
    }
}
