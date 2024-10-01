package com.auth.wow.libre.domain.ports.out.account;

import com.auth.wow.libre.infrastructure.entities.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface ObtainAccountPort {
    Optional<AccountEntity> findByUsername(String username);

    List<AccountEntity> findByUserId(Long id);

    Optional<AccountEntity> findById(Long id);
}
