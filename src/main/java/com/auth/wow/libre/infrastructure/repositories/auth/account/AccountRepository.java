package com.auth.wow.libre.infrastructure.repositories.auth.account;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByUsername(String username);

    List<AccountEntity> findByUserId(Long id);

    Optional<AccountEntity> findByIdAndUserId(Long Long, Long userId);

    Page<AccountEntity> findAllByEmailContainingIgnoreCase(String email, Pageable pageable);
}
