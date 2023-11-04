package com.auth.wow.libre.infrastructure.repositories.account;

import com.auth.wow.libre.infrastructure.entities.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Integer> {
    AccountEntity findByUsername(String username);
}
