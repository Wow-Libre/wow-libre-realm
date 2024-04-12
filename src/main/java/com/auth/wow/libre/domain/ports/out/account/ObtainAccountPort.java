package com.auth.wow.libre.domain.ports.out.account;

import com.auth.wow.libre.domain.model.Account;

public interface ObtainAccountPort {
    Account findByUsername(String username);
    Account findByEmail(String email);
}
