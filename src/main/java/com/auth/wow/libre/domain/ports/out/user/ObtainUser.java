package com.auth.wow.libre.domain.ports.out.user;

import com.auth.wow.libre.infrastructure.entities.auth.*;

import java.util.*;

public interface ObtainUser {
    Optional<UserEntity> findByUsername(String username);
}
