package com.auth.wow.libre.domain.model;

import com.auth.wow.libre.domain.model.enums.*;
import lombok.*;

import java.time.*;


@Builder
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
    public final String reference;
    public final boolean successful;
}
