package com.auth.wow.libre.infrastructure.repositories.characters.character_transaction;

import com.auth.wow.libre.domain.ports.out.character_transaction.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaCharacterTransactionAdapter implements SaveCharacterTransaction, ObtainCharacterTransaction {
    private final CharacterTransactionRepository characterTransactionRepository;

    public JpaCharacterTransactionAdapter(CharacterTransactionRepository characterTransactionRepository) {
        this.characterTransactionRepository = characterTransactionRepository;
    }

    @Override
    public void save(CharacterTransactionEntity transactionEntity, String transactionId) {
        characterTransactionRepository.save(transactionEntity);
    }

    @Override
    public List<CharacterTransactionEntity> getCharacterIdTransaction(Long characterId, String transactionId) {
        return characterTransactionRepository.findByCharacterIdAndStatusIsTrueAndIndebtednessIsTrue(characterId);
    }

    @Override
    public Optional<CharacterTransactionEntity> findByReference(String reference, String transactionId) {
        return characterTransactionRepository.findByReference(reference);
    }

    @Override
    public List<CharacterTransactionEntity> findByTransactionType(String transactionType, String transactionId) {
        return characterTransactionRepository.findByTransactionTypeAndStatusIsTrue(transactionType);
    }


}
