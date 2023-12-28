package com.auth.wow.libre.domain.ports.in.account;

import com.auth.wow.libre.domain.model.UpdateAccount;
import com.auth.wow.libre.domain.model.dto.AccountDetail;
import com.auth.wow.libre.domain.model.dto.AccountDto;

public interface AccountPort {
  void create(AccountDto account, String transactionId);

  AccountDetail obtain(String username, String transactionId);

  void updated(String username, UpdateAccount account, String transactionId);
}
