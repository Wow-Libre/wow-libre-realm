package com.auth.wow.libre.domain.ports.out.client;

import com.auth.wow.libre.infrastructure.entities.auth.*;

public interface SaveClient {

    void save(ClientEntity client);
}
