package com.auth.wow.libre.domain.ports.in.user;

public interface UserPort {

    void create(String username, String password, byte[] salt, String emulator, String apikey,
                Integer expansionId, String gmUsername, String gmPassword, String transactionId);

}
