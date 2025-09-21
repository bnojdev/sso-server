package io.github.bnojdev.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.core.io.ClassPathResource;
import java.security.interfaces.RSAPublicKey;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.nio.file.Files;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/h2-console/**", "/home","/otp","/home", "/css/**", "/js/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
            )
            .csrf(csrf -> csrf.disable())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            // Load public key from resources
            String publicKeyPEM = new String(Files.readAllBytes(new ClassPathResource("public.pem").getFile().toPath()));
            publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----","").replace("-----END PUBLIC KEY-----","").replaceAll("\\s","");
            byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(keySpec);
            return NimbusJwtDecoder.withPublicKey(publicKey).build();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA public key for JWT decoder", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
