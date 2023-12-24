package com.auth.wow.libre.infrastructure.repositories.account;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.model.exception.NotFoundException;
import com.auth.wow.libre.domain.ports.out.account.LoadAccountPort;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import com.auth.wow.libre.domain.ports.out.account.UpdateAccountPort;
import com.auth.wow.libre.infrastructure.entities.AccountEntity;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class JpaAccountAdapter implements LoadAccountPort, ObtainAccountPort, UpdateAccountPort {

  private final AccountRepository accountRepository;

  public JpaAccountAdapter(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public void create(Account account, AccountWebEntity accountWeb) {
    accountRepository.save(AccountEntity.fromDomainModel(account, accountWeb));
  }

  @Override
  public Account findByUsername(String username) {
    return accountRepository.findByUsername(username).map(AccountEntity::toDomainModel).orElse(null);
  }

  @Override
  public Account update(Account account, String transactionId) {

    Optional<AccountEntity> accountEntity = accountRepository.findByUsername(account.username);

    if (accountEntity.isEmpty()) {
      throw new NotFoundException("No data associated with this account found", transactionId);
    }

    AccountEntity accountUpdate = accountEntity.get();
    accountUpdate.setEmail(account.email != null ? account.email : accountUpdate.getEmail());
    accountUpdate.setUsername(account.username != null ? account.username : accountUpdate.getUsername());
    accountUpdate.setSalt(account.salt != null ? account.salt : accountUpdate.getSalt());
    accountUpdate.setVerifier(account.verifier != null ? account.verifier : accountUpdate.getVerifier());
    accountRepository.save(accountUpdate);

    return accountUpdate.toDomainModel();
  }

}
