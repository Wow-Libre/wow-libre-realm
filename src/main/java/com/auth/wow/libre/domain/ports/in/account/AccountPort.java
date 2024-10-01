package com.auth.wow.libre.domain.ports.in.account;

public interface AccountPort {
    void create(String username, String password, String email, String apiKey, boolean rebuildUsername, Long userId,
                String transactionId) ;

    Boolean isOnline(Long accountId, String transactionId);

}
