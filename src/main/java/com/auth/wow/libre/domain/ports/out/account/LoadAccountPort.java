package com.auth.wow.libre.domain.ports.out.account;

import com.auth.wow.libre.domain.model.Account;

public interface LoadAccountPort {
    void save(Account account);
}
