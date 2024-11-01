package com.auth.wow.libre.application.services.character_transaction;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.ports.in.character_service.*;
import com.auth.wow.libre.domain.ports.out.character_transaction.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CharacterTransactionService implements CharacterTransactionPort {
    private final ObtainCharacterTransaction obtainCharacterTransaction;
    private final SaveCharacterTransaction saveCharacterTransaction;
    private final RandomString randomString;

    public CharacterTransactionService(ObtainCharacterTransaction obtainCharacterTransaction,
                                       SaveCharacterTransaction saveCharacterTransaction,
                                       @Qualifier("random-string") RandomString randomString) {
        this.obtainCharacterTransaction = obtainCharacterTransaction;
        this.saveCharacterTransaction = saveCharacterTransaction;
        this.randomString = randomString;
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
        createTransaction.setTransactionType(transaction.transactionType.name());
        createTransaction.setReference(transaction.reference != null ? transaction.reference :
                randomString.nextString());
        saveCharacterTransaction.save(createTransaction, transactionId);
    }

    @Override
    public boolean existTransaction(String reference, String transactionId) {
        return obtainCharacterTransaction.findByReference(reference, transactionId).isPresent();
    }

    @Override
    public List<CharacterTransactionEntity> findByTransactionType(TransactionType transactionType,
                                                                  String transactionId) {
        return obtainCharacterTransaction.findByTransactionType(transactionType.name(), transactionId);
    }

    @Override
    public void update(CharacterTransactionEntity characterTransaction, String transactionId) {
        saveCharacterTransaction.save(characterTransaction, transactionId);
    }

}
