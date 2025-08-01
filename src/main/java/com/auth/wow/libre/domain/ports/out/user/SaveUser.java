package com.auth.wow.libre.domain.ports.out.user;

import com.auth.wow.libre.infrastructure.entities.auth.*;

public interface SaveUser {

    void save(UserEntity client);
}
