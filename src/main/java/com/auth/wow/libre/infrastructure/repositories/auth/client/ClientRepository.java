package com.auth.wow.libre.infrastructure.repositories.auth.client;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByUsername(String username);

    Optional<ClientEntity> findByStatusIsTrue();

    List<ClientEntity> findByRol(String rol);
}
