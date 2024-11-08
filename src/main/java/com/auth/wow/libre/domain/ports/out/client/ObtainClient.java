package com.auth.wow.libre.domain.ports.out.client;

import com.auth.wow.libre.infrastructure.entities.auth.*;

import java.util.*;

public interface ObtainClient {
    Optional<ClientEntity> findByUsername(String username);
    Optional<ClientEntity> getClientStatusIsTrue();

}
