package com.auth.wow.libre.domain.ports.in.bank;

public interface BankPort {
    Double collectGold(Long userId, Double moneyToPay, String transactionId);

}
