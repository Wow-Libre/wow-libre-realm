package com.auth.wow.libre.infrastructure.repositories.account_web;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.ports.out.account_web.LoadAccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.ObtainAccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.UpdateAccountWebPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.stereotype.Service;

@Service
public class JpaAccountWebAdapter implements LoadAccountWebPort, UpdateAccountWebPort, ObtainAccountWebPort {

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

  @Override
  public void update(AccountWebEntity account) {
    accountWebRepository.save(account);
  }


  @Override
  public AccountWebEntity findById(Long id) {
    return accountWebRepository.findById(id).orElse(null);
  }
}
