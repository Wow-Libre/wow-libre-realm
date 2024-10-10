package com.auth.wow.libre.domain.ports.out.character_transaction;

import com.auth.wow.libre.infrastructure.entities.characters.*;

public interface SaveCharacterTransaction {
    void save(CharacterTransactionEntity transactionEntity, String transactionId);
}
