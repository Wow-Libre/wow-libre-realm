package com.auth.wow.libre.application.services.account_web;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.LoadAccountWebPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountWebService implements AccountWebPort {

  private final LoadAccountWebPort loadAccountWebPort;


  public AccountWebService(LoadAccountWebPort loadAccountWebPort) {
    this.loadAccountWebPort = loadAccountWebPort;
  }

  @Override
  public AccountWebEntity create(Account account) {
    return loadAccountWebPort.save(account);
  }

}
