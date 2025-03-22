package com.auth.wow.libre.domain.ports.in.client;

public interface ClientPort {

    void create(String username, String password, byte[] salt, String transactionId);

    void create(String username, String password);

    String login(String username, String password);

    boolean isValidJwt(String jwt);

    String username(String jwt);

    void changePassword(String username, String password, String newPassword);

}
