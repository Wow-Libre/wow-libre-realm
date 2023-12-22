package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.model.UpdateAccount;
import com.auth.wow.libre.domain.model.dto.AccountDto;
import com.auth.wow.libre.domain.model.exception.BadRequestException;
import com.auth.wow.libre.domain.model.exception.FoundException;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import com.auth.wow.libre.domain.ports.in.account_web.AccountWebPort;
import com.auth.wow.libre.domain.ports.out.account.LoadAccountPort;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
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
  public void create(AccountDto account, String transactionId) {

    if (getAccount(account.getUsername()) != null) {
      throw new FoundException("There is already a registered client with this data", transactionId);
    }

    try {
      byte[] verifier = Hex.decodeHex(account.getVerifier());
      byte[] salt = Hex.decodeHex(account.getSalt());

      Account registerAccount = Account.builder()
              .password(passwordEncoder.encode(account.getPassword()))
              .email(account.getEmail())
              .salt(salt)
              .verifier(verifier)
              .country(account.getCountry())
              .dateOfBirth(account.getDateOfBirth())
              .cellPhone(account.getCellPhone())
              .lastName(account.getLastName())
              .firstName(account.getFirstName())
              .build();
      AccountWebEntity accountWeb = accountWebPort.save(registerAccount, transactionId);

      loadAccountPort.create(registerAccount, accountWeb);
    } catch (DecoderException e) {
      throw new BadRequestException("Ha ocurrido un error con el cifrado.", transactionId);
    }
  }

  @Override
  public Account obtain(String username, String transactionId) {
    return obtainAccountPort.findByUsername(username);
  }

  @Override
  public void updated(String username, UpdateAccount account, String transactionId) {
    Account accountFound = getAccount(username);

    if (accountFound == null) {
      throw new FoundException("There is already a registered client with this data", transactionId);
    }

    Account registerAccount = Account.builder()
            .accountWebId(accountFound.accountWebId)
            .email(account.getEmail())
            .password(accountFound.password)
            .country(account.getCountry())
            .dateOfBirth(account.getDateOfBirth())
            .cellPhone(account.getCellPhone())
            .lastName(account.getLastName())
            .firstName(account.getFirstName())
            .build();

    accountWebPort.update(registerAccount, transactionId);

  }

  private Account getAccount(String username) {
    return obtainAccountPort.findByUsername(username);
  }

}
