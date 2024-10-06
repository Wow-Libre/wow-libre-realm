package com.auth.wow.libre.infrastructure.repositories.auth.account_banned;

import com.auth.wow.libre.infrastructure.entities.auth.AccountBannedEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountBannedRepository extends CrudRepository<AccountBannedEntity, Long> {
  Optional<AccountBannedEntity> findByAccountIdAndActiveIsTrue(long id);
}
