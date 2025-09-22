package io.github.bnojdev.sso.controller;

import io.github.bnojdev.sso.model.Client;
import io.github.bnojdev.sso.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/register-client")
public class ClientRegistrationController {
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<?> registerClient(@RequestBody Client client) {
        // Generate clientId and clientSecret if not provided
        if (client.getClientId() == null || client.getClientId().isEmpty()) {
            client.setClientId(UUID.randomUUID().toString());
        }
        if (client.getClientSecret() == null || client.getClientSecret().isEmpty()) {
            client.setClientSecret(UUID.randomUUID().toString());
        }
        // Save client to DB
        clientRepository.save(client);
        return ResponseEntity.ok(client);
    }
}

