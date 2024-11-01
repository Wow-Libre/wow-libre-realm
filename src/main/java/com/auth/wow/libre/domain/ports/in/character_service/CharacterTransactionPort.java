package com.auth.wow.libre.domain.ports.in.character_service;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface CharacterTransactionPort {
    boolean availableDebt(Long characterId, Double money, Double cost, String transactionId);

    void create(CharacterTransactionModel transactionEntity, String transactionId);

    boolean existTransaction(String reference, String transactionId);

    List<CharacterTransactionEntity> findByTransactionType(TransactionType transactionType, String transactionId);

    void update(CharacterTransactionEntity characterTransaction, String transactionId);
}
