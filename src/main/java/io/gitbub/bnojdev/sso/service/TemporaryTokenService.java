package io.github.bnojdev.sso.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TemporaryTokenService {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION = 5 * 60 * 1000; // 5 minutes
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private static final Logger logger = LoggerFactory.getLogger(TemporaryTokenService.class);

    public String generateToken(String subject) {
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
        logger.info("Generated token for subject: {} with expiry: {}", subject, getExpiry(token));
        return token;
    }

    public Date getExpiry(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            logger.debug("Extracted expiry for token: {}", claims.getExpiration());
            return claims.getExpiration();
        } catch (Exception e) {
            logger.error("Failed to extract expiry from token: {}", e.getMessage());
            return null;
        }
    }

    public String refreshToken(String token) {
        if (!validateToken(token)) {
            logger.warn("Attempted to refresh invalid token");
            return null;
        }
        String subject = getSubject(token);
        String newToken = generateToken(subject);
        logger.info("Refreshed token for subject: {}", subject);
        return newToken;
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
        logger.info("Token invalidated for subject: {}", getSubject(token));
    }

    public String getSubject(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            logger.debug("Extracted subject from token: {}", claims.getSubject());
            return claims.getSubject();
        } catch (Exception e) {
            logger.error("Failed to extract subject from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        if (blacklistedTokens.contains(token)) {
            logger.warn("Token validation failed: token is blacklisted");
            return false;
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            logger.info("Token validated for subject: {}", getSubject(token));
            return true;
        } catch (Exception e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
