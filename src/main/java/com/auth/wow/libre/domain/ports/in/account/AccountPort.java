package com.auth.wow.libre.domain.ports.in.account;

import com.auth.wow.libre.domain.model.Account;

public interface AccountPort {
    void create(Account account);

    Account Obtain(String username);
}
