package com.auth.wow.libre.domain.ports.out.account;

import com.auth.wow.libre.infrastructure.entities.auth.AccountEntity;

public interface SaveAccountPort {
    AccountEntity save(AccountEntity account);
}
