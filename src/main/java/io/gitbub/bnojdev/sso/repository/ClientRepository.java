package io.github.bnojdev.sso.repository;

import io.github.bnojdev.sso.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByClientId(String clientId);
}

