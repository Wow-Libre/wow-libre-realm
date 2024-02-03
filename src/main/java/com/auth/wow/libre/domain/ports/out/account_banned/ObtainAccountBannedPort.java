package com.auth.wow.libre.domain.ports.out.account_banned;

import com.auth.wow.libre.domain.model.AccountBanned;

public interface ObtainAccountBannedPort {
  AccountBanned getAccountBanned(Long accountId);
}
