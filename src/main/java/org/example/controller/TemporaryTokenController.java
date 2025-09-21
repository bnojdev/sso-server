package org.example.controller;

import org.example.service.TemporaryTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/temporary-token")
public class TemporaryTokenController {
    @Autowired
    private TemporaryTokenService temporaryTokenService;

    private static final Logger logger = LoggerFactory.getLogger(TemporaryTokenController.class);

    @PostMapping("/issue")
    public ResponseEntity<?> issueToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("Token issue requested for user: {}", username);
        String token = temporaryTokenService.generateToken(username);
        Date expiry = temporaryTokenService.getExpiry(token);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("expiry", expiry != null ? expiry.getTime() : null);
        logger.info("Token issued for user: {} with expiry: {}", username, expiry);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        logger.info("Token validation requested");
        boolean valid = temporaryTokenService.validateToken(token);
        Date expiry = temporaryTokenService.getExpiry(token);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", valid);
        response.put("expiry", expiry != null ? expiry.getTime() : null);
        logger.info("Token validation result: {} (expiry: {})", valid, expiry);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String token) {
        logger.info("Token refresh requested");
        String newToken = temporaryTokenService.refreshToken(token);
        if (newToken == null) {
            logger.warn("Token refresh failed: invalid token");
            return ResponseEntity.badRequest().body("Invalid token");
        }
        Date expiry = temporaryTokenService.getExpiry(newToken);
        Map<String, Object> response = new HashMap<>();
        response.put("token", newToken);
        response.put("expiry", expiry != null ? expiry.getTime() : null);
        logger.info("Token refreshed with new expiry: {}", expiry);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/invalidate")
    public ResponseEntity<?> invalidateToken(@RequestParam String token) {
        logger.info("Token invalidate requested");
        temporaryTokenService.invalidateToken(token);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Token invalidated");
        logger.info("Token invalidated");
        return ResponseEntity.ok(response);
    }

}
