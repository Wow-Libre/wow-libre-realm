package com.auth.wow.libre.domain.ports.out.account;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;

public interface LoadAccountPort {
  void save(Account account, AccountWebEntity accountWeb);
}
