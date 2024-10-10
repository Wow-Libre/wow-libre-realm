package com.auth.wow.libre.domain.ports.in.character_service;

import com.auth.wow.libre.domain.model.*;

public interface CharacterTransactionPort {
    boolean availableDebt(Long characterId, Double money, Double cost, String transactionId);

    void create(CharacterTransactionModel transactionEntity, String transactionId);

}
