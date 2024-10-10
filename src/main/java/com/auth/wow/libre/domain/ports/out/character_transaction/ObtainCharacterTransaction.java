package com.auth.wow.libre.domain.ports.out.character_transaction;

import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ObtainCharacterTransaction {
    List<CharacterTransactionEntity> getCharacterIdTransaction(Long characterId, String transactionId);


}
