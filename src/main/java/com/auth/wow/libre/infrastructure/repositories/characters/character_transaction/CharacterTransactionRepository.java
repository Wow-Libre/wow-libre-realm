package com.auth.wow.libre.infrastructure.repositories.characters.character_transaction;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface CharacterTransactionRepository extends CrudRepository<CharacterTransactionEntity, Long> {
    List<CharacterTransactionEntity> findByCharacterIdAndStatusIsTrueAndIndebtednessIsTrue(Long characterId);

    Optional<CharacterTransactionEntity> findByReference(String reference);

    List<CharacterTransactionEntity> findByTransactionTypeAndStatusIsTrue(String transactionType);
}
