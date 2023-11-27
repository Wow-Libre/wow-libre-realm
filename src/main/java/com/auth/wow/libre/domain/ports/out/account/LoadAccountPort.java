package com.auth.wow.libre.domain.ports.out.account;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.apache.commons.codec.DecoderException;

public interface LoadAccountPort {
  void save(Account account, AccountWebEntity accountWeb) throws DecoderException;
}
