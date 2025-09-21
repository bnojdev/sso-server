package org.example.controller;

import jakarta.servlet.http.HttpSession;
import org.example.model.GenericResponse;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public Object register(@RequestHeader(value = "Referer", required = false) String referer, RedirectAttributes attributes) {
        if (referer == null || !referer.contains("/login")) {
            attributes.addFlashAttribute("error", "You must access registration from the login page.");
            return new RedirectView("/login");
        }
        return "register";
    }

    @PostMapping("/otp")
    public Object verifyOtp(@RequestParam("otp") String otp,
                           HttpSession session,
                           Model model) {

        String username = (String) session.getAttribute("otpUsername");
        Boolean val = userService.otpValidation(otp, username);
        if (val != null && val) {
            session.removeAttribute("otpUsername");
            return new RedirectView("/home");
        } else {
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
            model.addAttribute("showResend", true);
            return "otp";
        }

    }

    @PostMapping("/register")
    public Object handleRegister(@RequestParam("e-mail") String email,
                                 @RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        // Save user to DB
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setActivated(false);
        user.setRegistrationTime(
                LocalDateTime.now()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
        );

        GenericResponse response = userService.registerClient(user);
        // Add popup message as flash attribute
        redirectAttributes.addFlashAttribute("registrationMessage", "Registration complete. Login first to activate your account within 30 seconds, else user details will be removed.");
        return new RedirectView("/login");
    }

    @GetMapping("/otp")
    public Object showOtpPage(HttpSession session, Model model) {
        Boolean allowed = (Boolean) session.getAttribute("otpAllowed");
        if (allowed == null || !allowed) {
            return new RedirectView("/login");
        }
        session.removeAttribute("otpAllowed");
        return "otp";
    }
}
