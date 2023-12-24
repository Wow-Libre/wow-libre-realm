package com.auth.wow.libre.application.services.account_web;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.model.exception.NotFoundException;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.LoadAccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.ObtainAccountWebPort;
import com.auth.wow.libre.domain.ports.out.account_web.UpdateAccountWebPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountWebService implements AccountWebPort {

  private final LoadAccountWebPort loadAccountWebPort;
  private final UpdateAccountWebPort updateAccountWebPort;
  private final ObtainAccountWebPort obtainAccountWebPort;

  public AccountWebService(LoadAccountWebPort loadAccountWebPort, UpdateAccountWebPort updateAccountWebPort,
                           ObtainAccountWebPort obtainAccountWebPort) {
    this.loadAccountWebPort = loadAccountWebPort;
    this.updateAccountWebPort = updateAccountWebPort;
    this.obtainAccountWebPort = obtainAccountWebPort;
  }

  @Override
  public AccountWebEntity save(Account account, String transactionId) {
    return loadAccountWebPort.save(account);
  }

  @Override
  public void update(Account account, Long accountIdWeb, String transactionId) {

    AccountWebEntity accountWeb = obtainAccountWebPort.findById(accountIdWeb);

    if (accountWeb == null) {
      throw new NotFoundException("There is no web account available to update", transactionId);
    }

    accountWeb.setCountry(account.country != null ? account.country : accountWeb.getCountry());
    accountWeb.setFirstName(account.firstName != null ? account.firstName : accountWeb.getFirstName());
    accountWeb.setLastName(account.lastName != null ? account.lastName : accountWeb.getLastName());
    accountWeb.setPassword(account.password != null ? account.password : accountWeb.getPassword());
    accountWeb.setDateOfBirth(account.dateOfBirth != null ? account.dateOfBirth : accountWeb.getDateOfBirth());
    accountWeb.setCellPhone(account.cellPhone != null ? account.cellPhone : accountWeb.getCellPhone());

    updateAccountWebPort.update(accountWeb);
  }

}
