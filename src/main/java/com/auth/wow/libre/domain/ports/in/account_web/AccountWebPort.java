package com.auth.wow.libre.domain.ports.in.account_web;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;

public interface AccountWebPort {
  AccountWebEntity create(Account account);
}
