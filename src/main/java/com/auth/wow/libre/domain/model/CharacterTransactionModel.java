package com.auth.wow.libre.domain.model;

import com.auth.wow.libre.domain.model.enums.*;

import java.time.*;


public class CharacterTransactionModel {

    public final Long id;
    public final TransactionType transactionType;
    public final Long characterId;
    public final Double amount;
    public final boolean indebtedness;
    public final boolean status;
    public final LocalDateTime transactionDate;
    public final String command;
    public final Long accountId;
    public final Long userId;
    public final boolean successful;

    public CharacterTransactionModel(Long id, TransactionType transactionType, Long characterId, Double amount,
                                     boolean indebtedness, boolean status, LocalDateTime transactionDate,
                                     String command, Long accountId, Long userId, boolean successful) {
        this.id = id;
        this.transactionType = transactionType;
        this.characterId = characterId;
        this.amount = amount;
        this.indebtedness = indebtedness;
        this.status = status;
        this.transactionDate = transactionDate;
        this.command = command;
        this.accountId = accountId;
        this.userId = userId;
        this.successful = successful;
    }

}
