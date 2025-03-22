package com.auth.wow.libre.infrastructure.repositories.auth.client;

import com.auth.wow.libre.domain.ports.out.client.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaClientAdapter implements ObtainClient, SaveClient {
    private final ClientRepository clientRepository;

    public JpaClientAdapter(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<ClientEntity> findByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    @Override
    public Optional<ClientEntity> getClientStatusIsTrue() {
        return clientRepository.findByStatusIsTrue();
    }

    @Override
    public List<ClientEntity> findByRolName(String rolType) {
        return clientRepository.findByRol(rolType);
    }

    @Override
    public void save(ClientEntity client) {
        clientRepository.save(client);
    }
}
