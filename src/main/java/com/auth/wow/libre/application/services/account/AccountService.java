package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.out.account.LoadAccountPort;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements AccountPort {

  private final LoadAccountPort loadAccountPort;
  private final ObtainAccountPort obtainAccountPort;

  private final AccountWebPort accountWebPort;

  public AccountService(LoadAccountPort loadAccountPort, ObtainAccountPort obtainAccountPort,
                        AccountWebPort accountWebPort) {
    this.loadAccountPort = loadAccountPort;
    this.obtainAccountPort = obtainAccountPort;
    this.accountWebPort = accountWebPort;
  }

  @Override
  public void create(Account account) {

    if (obtainAccountPort.findByUsername(account.getUsername()) != null) {
      throw new RuntimeException("El cliente ya existe");
    }
    AccountWebEntity accountWeb = accountWebPort.create(account);
    loadAccountPort.save(account, accountWeb);
  }

  @Override
  public Account Obtain(String username) {
    return obtainAccountPort.findByUsername(username);
  }

  @Override
  public void updated(String username, Account account) {
    Account accountFound = obtainAccountPort.findByUsername(account.getUsername());

    if (accountFound == null) {
      throw new RuntimeException("No existe el cliente");
    }

    accountFound.setCellPhone(account.getCellPhone());
    accountFound.setCountry(account.getCountry());
    accountFound.setDateOfBirth(account.getDateOfBirth());
    accountFound.setFirstName(account.getFirstName());
    accountFound.setLastName(account.getLastName());

  }
}
