package com.auth.wow.libre.domain.ports.in.account;

import com.auth.wow.libre.domain.model.Account;

public interface CreateAccountPort {
    void create(Account account);
}
