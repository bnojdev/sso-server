package io.github.bnojdev.sso.repository;

import io.github.bnojdev.sso.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseRegisteredClientRepository implements RegisteredClientRepository {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        // Optional: implement if you want to allow saving RegisteredClient
    }

    @Override
    public RegisteredClient findById(String id) {
        Client client = clientRepository.findByClientId(id);
        return client != null ? toRegisteredClient(client) : null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Client client = clientRepository.findByClientId(clientId);
        return client != null ? toRegisteredClient(client) : null;
    }

    private RegisteredClient toRegisteredClient(Client client) {
        RegisteredClient.Builder builder = RegisteredClient.withId(client.getClientId())
            .clientId(client.getClientId())
            .clientSecret(client.getClientSecret())
            .clientAuthenticationMethod(org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        // Add grant types
        if (client.getGrantTypes() != null) {
            for (String grant : client.getGrantTypes().split(",")) {
                builder.authorizationGrantType(new org.springframework.security.oauth2.core.AuthorizationGrantType(grant.trim()));
            }
        }
        // Add scopes
        if (client.getScopes() != null) {
            for (String scope : client.getScopes().split(",")) {
                builder.scope(scope.trim());
            }
        }
        // Add redirect URI
        if (client.getRedirectUri() != null) {
            builder.redirectUri(client.getRedirectUri());
        }
        return builder.build();
    }
}

