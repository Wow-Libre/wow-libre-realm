package com.auth.wow.libre.domain.ports.in.account;

import com.auth.wow.libre.domain.model.dto.*;

public interface AccountPort {
    Long create(String username, String password, String email, Long userId,
                String expansion, byte[] salt, String transactionId);

    Boolean isOnline(Long accountId, String transactionId);

    AccountDetailDto account(Long accountId, String transactionId);

    void changePassword(Long accountId, Long userId, String password, byte[] salt, String transactionId);
}
