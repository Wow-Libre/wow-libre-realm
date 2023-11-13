package com.auth.wow.libre.infrastructure.repositories.account_web;

import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountWebRepository extends CrudRepository<AccountWebEntity, Integer> {
}
