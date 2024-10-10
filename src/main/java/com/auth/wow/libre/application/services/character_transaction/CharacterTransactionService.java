package com.auth.wow.libre.application.services.character_transaction;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.in.character_service.*;
import com.auth.wow.libre.domain.ports.out.character_transaction.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CharacterTransactionService implements CharacterTransactionPort {
    private final ObtainCharacterTransaction obtainCharacterTransaction;
    private final SaveCharacterTransaction saveCharacterTransaction;

    public CharacterTransactionService(ObtainCharacterTransaction obtainCharacterTransaction,
                                       SaveCharacterTransaction saveCharacterTransaction) {
        this.obtainCharacterTransaction = obtainCharacterTransaction;
        this.saveCharacterTransaction = saveCharacterTransaction;
    }

    @Override
    public boolean availableDebt(Long characterId, Double money, Double cost, String transactionId) {

        List<CharacterTransactionEntity> characterTransaction =
                obtainCharacterTransaction.getCharacterIdTransaction(characterId, transactionId);

        double due =
                characterTransaction
                        .stream().mapToDouble(CharacterTransactionEntity::getAmount).sum();

        return money >= due + cost || (money == 0 && cost == 0) || (due == 0);
    }


    @Override
    public void create(CharacterTransactionModel transaction, String transactionId) {
        CharacterTransactionEntity createTransaction = new CharacterTransactionEntity();
        createTransaction.setCharacterId(transaction.characterId);
        createTransaction.setTransactionId(transactionId);
        createTransaction.setStatus(transaction.status);
        createTransaction.setTransactionDate(transaction.transactionDate);
        createTransaction.setIndebtedness(transaction.indebtedness);
        createTransaction.setAmount(transaction.amount);
        createTransaction.setCommand(transaction.command);
        createTransaction.setUserId(transaction.userId);
        createTransaction.setAccountId(transaction.accountId);
        createTransaction.setSuccessful(transaction.successful);
        saveCharacterTransaction.save(createTransaction, transactionId);
    }

}
