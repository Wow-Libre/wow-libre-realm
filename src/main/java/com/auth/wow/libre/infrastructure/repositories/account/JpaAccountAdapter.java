package com.auth.wow.libre.infrastructure.repositories.account;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.ports.out.account.LoadAccountPort;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import com.auth.wow.libre.infrastructure.entities.AccountEntity;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JpaAccountAdapter implements LoadAccountPort, ObtainAccountPort {

  private final AccountRepository accountRepository;

  public JpaAccountAdapter(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public void save(Account account, AccountWebEntity accountWeb) {
    accountRepository.save(AccountEntity.fromDomainModel(account, accountWeb));
  }

  @Override
  public Account findByUsername(String username) {
    AccountEntity accountFound = accountRepository.findByUsername(username);
    return accountFound != null ? accountFound.toDomainModel() : null;
  }


}
