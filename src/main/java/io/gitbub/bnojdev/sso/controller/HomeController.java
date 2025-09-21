package io.github.bnojdev.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails user, Model model) {
        String username = user != null ? user.getUsername() : "Guest";
        log.info("Home page accessed by: {}", username);
        model.addAttribute("username", username);
        return "home";
    }
}
