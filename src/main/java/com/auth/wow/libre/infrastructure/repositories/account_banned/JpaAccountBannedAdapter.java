package com.auth.wow.libre.infrastructure.repositories.account_banned;

import com.auth.wow.libre.domain.model.AccountBanned;
import com.auth.wow.libre.domain.ports.out.account_banned.ObtainAccountBannedPort;
import com.auth.wow.libre.infrastructure.entities.AccountBannedEntity;
import org.springframework.stereotype.Component;

@Component
public class JpaAccountBannedAdapter implements ObtainAccountBannedPort {

  private final AccountBannedRepository accountBannedRepository;

  public JpaAccountBannedAdapter(AccountBannedRepository accountBannedRepository) {
    this.accountBannedRepository = accountBannedRepository;
  }

  @Override
  public AccountBanned getAccountBanned(Long accountId) {
    return accountBannedRepository.findByAccountIdAndActiveIsTrue(accountId).map(this::mapToModel).orElse(null);
  }

  private AccountBanned mapToModel(AccountBannedEntity accountBannedEntity) {
    return new AccountBanned(
        accountBannedEntity.getAccountId(),
        new java.util.Date(accountBannedEntity.getBandate() * 1000),
        new java.util.Date(accountBannedEntity.getUnbandate() * 1000),
        accountBannedEntity.getBannedby(),
        accountBannedEntity.getBanreason(),
        accountBannedEntity.getActive() == 1
    );
  }
}
