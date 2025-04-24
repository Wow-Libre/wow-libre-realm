package com.auth.wow.libre.infrastructure.repositories.auth.account;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByUsername(String username);

    List<AccountEntity> findByUserId(Long id);

    Optional<AccountEntity> findByIdAndUserId(Long Long, Long userId);

    Page<AccountEntity> findAllByEmailContainingIgnoreCase(String email, Pageable pageable);

    Long countByOnlineTrue();

    @Query("SELECT new com.auth.wow.libre.infrastructure.repositories.auth.account.MetricsProjection("
            + "COUNT(a), "  // Contar el total de usuarios
            + "SUM(CASE WHEN a.online = true THEN 1 ELSE 0 END), "
            + "SUM(CASE WHEN a.userId IS NOT NULL THEN 1 ELSE 0 END)) "
            + "FROM AccountEntity a")
    MetricsProjection fetchMetrics();

    Optional<AccountEntity> findByEmail(String email);

}
