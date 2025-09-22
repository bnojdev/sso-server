package io.github.bnojdev.sso.service.impl;

import io.github.bnojdev.sso.model.GenericResponse;
import io.github.bnojdev.sso.model.User;
import io.github.bnojdev.sso.repository.UserRepository;
import io.github.bnojdev.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Boolean userValidation(String username, String password) {
        log.info("Validating user: {}", username);
        User user = userRepository.findByUsername(username);
        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            log.info("User validation successful for username: {}", username);
            return true;
        } else {
            log.warn("User validation failed for username: {}", username);
            return false;
        }
    }

    @Override
    public Boolean otpValidation(String otp, String username) {
        log.info("OTP validation attempt for username: {} with OTP: {}", username, otp);
        String correctOtp = "686856";
        if (otp != null && otp.equals(correctOtp) && username != null){
            User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setActivated(true);
                userRepository.save(user);
                log.info("OTP validation successful for username: {}", username);
                return true;
            }
        }
        log.warn("OTP validation failed for username: {}", username);
        return false;
    }

    @Override
    public GenericResponse registerClient(User user) {
        log.info("Registering user: {}", user.getUsername());
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            log.warn("Registration failed: Username already exists: {}", user.getUsername());
            return new GenericResponse(false, "Username already exists.",1);
        }
        User existingEmail = userRepository.findByEmail(user.getEmail());
        if (existingEmail != null) {
            log.warn("Registration failed: Email already registered: {}", user.getEmail());
            return new GenericResponse(false, "Email already registered.",1);
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        log.info("Password hashed for user: {}", user.getUsername());
        userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());
        return new GenericResponse(true, "User registered successfully.",0);
    }

    @Override
    public void removeUnactivatedUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty() ) {
            log.info("Checking for unactivated users to remove. Total users: {}", users.size());
            for (User user : users) {
                if (user.getActivated() != null && !user.getActivated()) {
                    Long time = user.getRegistrationTime();
                    Long currentTime = LocalDateTime.now()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli();
                    if(currentTime > time + 30000){
                        userRepository.delete(user);
                        log.info("Removed unactivated user: {}", user.getUsername());
                    }
                }
            }
        } else {
            log.info("No unactivated users found.");
        }
    }


}
