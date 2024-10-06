package com.auth.wow.libre.domain.ports.in.account;

public interface AccountPort {
    Long create(String username, String password, String email, Long userId,
                String expansion, byte[] salt, String transactionId);

    Boolean isOnline(Long accountId, String transactionId);

}
