package com.auth.wow.libre.domain.ports.in.account;

import com.auth.wow.libre.domain.model.dto.AccountChangePasswordDto;
import com.auth.wow.libre.domain.model.dto.AccountDetailDto;
import com.auth.wow.libre.domain.model.dto.AccountGameDto;
import com.auth.wow.libre.domain.model.dto.AccountsDetailDto;

import java.util.List;

public interface AccountPort {

    List<AccountsDetailDto> availableAccounts(String email, String transactionId);

    void createGameAccount(AccountGameDto accountGameDto, String email, String transactionId);

    AccountDetailDto accountDetail(Long accountId, String email, String transactionId);

    void changePasswordAccountGame(AccountChangePasswordDto accountChangePasswordDto, String email,
                                   String transactionId);
    boolean findByIdAndAccountWebId(Long id, Long accountWebId, String transactionId);

}
