package com.auth.wow.libre.domain.ports.in.user;

public interface UserPort {

    void create(String username, String password, String emulator, Long realmId,
                Integer expansionId, String transactionId);

    void delete(String username, String transactionId);

}
