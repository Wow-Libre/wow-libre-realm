package com.auth.wow.libre.domain.strategy.accounts;

import lombok.*;

@Data
public abstract class Account {
    private Long id;
    private String username;

    public abstract void create(String username, String password, String email, Long userId,
                                String transactionId);

    public abstract void changePassword(String username, String password, String email, String transactionId);

}
