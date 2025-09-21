package io.github.bnojdev.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import io.github.bnojdev.sso.repository.DatabaseRegisteredClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

@Configuration
public class AuthorizationServerConfig {
    @Autowired
    private DatabaseRegisteredClientRepository databaseRegisteredClientRepository;

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.build();
    }

    @Bean
    @Primary
    public RegisteredClientRepository registeredClientRepository() {
        return databaseRegisteredClientRepository;
    }
}
