package com.auth.wow.libre.domain.ports.in.account;

import com.auth.wow.libre.domain.model.dto.AccountGameDto;
import com.auth.wow.libre.domain.model.dto.AccountWebDto;
import com.auth.wow.libre.domain.model.dto.AccountsDetailDto;
import com.auth.wow.libre.domain.model.security.JwtDto;

import java.util.List;

public interface AccountPort {
    JwtDto createWebAccount(AccountWebDto accountWebDto, String transactionId);

    boolean isEmailExists(String email, String transactionId);

    List<AccountsDetailDto> availableAccounts(String email, String transactionId);

    void createGameAccount(AccountGameDto accountGameDto, String email, String transactionId);

}
