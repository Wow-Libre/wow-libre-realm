package com.auth.wow.libre.domain.ports.in.client;

public interface ClientPort {

    void create(String username, String password, byte[] salt, String transactionId) ;
}
