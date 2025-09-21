package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpSession;

@Controller
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public Object handleLogin(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        log.info("Login attempt for username: {}", username);
        Boolean val = userService.userValidation(username,password);
        if (val != null && val) {
            log.info("Login successful for username: {}. Redirecting to OTP.", username);
            session.setAttribute("otpUsername", username);
            session.setAttribute("otpAllowed", true);
            return new RedirectView("/otp");
        } else {
            log.warn("Login failed for username: {}.", username);
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            return new RedirectView("/login");
        }
    }
}
