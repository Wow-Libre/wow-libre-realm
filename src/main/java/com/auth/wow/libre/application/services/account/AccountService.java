package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.model.exception.BadRequestException;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.out.account.LoadAccountPort;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements AccountPort {

  private final LoadAccountPort loadAccountPort;
  private final ObtainAccountPort obtainAccountPort;
  private final AccountWebPort accountWebPort;
  private final PasswordEncoder passwordEncoder;

  public AccountService(LoadAccountPort loadAccountPort, ObtainAccountPort obtainAccountPort,
                        AccountWebPort accountWebPort, PasswordEncoder passwordEncoder) {
    this.loadAccountPort = loadAccountPort;
    this.obtainAccountPort = obtainAccountPort;
    this.accountWebPort = accountWebPort;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void create(Account account, String transactionId) {

    if (getAccount(account.getUsername()) != null) {
      throw new BadRequestException("El cliente ya existe", transactionId);
    }

    account.setPassword(passwordEncoder.encode(account.getPassword()));
    AccountWebEntity accountWeb = accountWebPort.create(account);
    loadAccountPort.save(account, accountWeb);
  }

  @Override
  public Account Obtain(String username, String transactionId) {
    return obtainAccountPort.findByUsername(username);
  }

  @Override
  public void updated(String username, Account account) {
    Account accountFound = getAccount(account.getUsername());

    if (accountFound == null) {
      throw new RuntimeException("No existe el cliente");
    }
    accountFound.setCellPhone(account.getCellPhone());
    accountFound.setCountry(account.getCountry());
    accountFound.setDateOfBirth(account.getDateOfBirth());
    accountFound.setFirstName(account.getFirstName());
    accountFound.setLastName(account.getLastName());

  }

  private Account getAccount(String username) {
    return obtainAccountPort.findByUsername(username);
  }

}
