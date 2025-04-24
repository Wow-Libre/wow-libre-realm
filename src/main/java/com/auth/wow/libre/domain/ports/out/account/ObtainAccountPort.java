package com.auth.wow.libre.domain.ports.out.account;

import com.auth.wow.libre.infrastructure.entities.auth.AccountEntity;
import com.auth.wow.libre.infrastructure.repositories.auth.account.*;

import java.util.List;
import java.util.Optional;

public interface ObtainAccountPort {
    Optional<AccountEntity> findByUsername(String username);

    Optional<AccountEntity> findByEmail(String email);

    List<AccountEntity> findByUserId(Long id);

    Optional<AccountEntity> findById(Long id);

    Optional<AccountEntity> findByIdAndUserId(Long id, Long userId);

    List<AccountEntity> findByAll(int size, int page, String filter);

    Long count();

    Long countOnline(String transactionId);

    MetricsProjection metrics(String transactionId);
}
