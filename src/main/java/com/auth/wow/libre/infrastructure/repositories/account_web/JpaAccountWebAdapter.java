package com.auth.wow.libre.infrastructure.repositories.account_web;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.ports.out.account_web.LoadAccountWebPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.stereotype.Service;

@Service
public class JpaAccountWebAdapter implements LoadAccountWebPort {

  private final AccountWebRepository accountWebRepository;

  public JpaAccountWebAdapter(AccountWebRepository accountWebRepository) {
    this.accountWebRepository = accountWebRepository;
  }

  @Override
  public AccountWebEntity save(Account account) {
    AccountWebEntity accountWeb = AccountWebEntity.fromDomainModel(account);
    accountWebRepository.save(accountWeb);
    return accountWeb;
  }


}
