package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.character_transaction.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.ports.out.character_transaction.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterTransactionServiceTest {
    @Mock
    private ObtainCharacterTransaction obtainCharacterTransaction;

    @Mock
    private SaveCharacterTransaction saveCharacterTransaction;

    @Mock
    private RandomString randomString;

    @InjectMocks
    private CharacterTransactionService characterTransactionService;

    private static final Long CHARACTER_ID = 100L;
    private static final Double MONEY = 500.0;
    private static final Double COST = 200.0;
    private static final String TRANSACTION_ID = "txn-123";
    private static final String REFERENCE = "ref-456";

    @BeforeEach
    void setUp() {
        characterTransactionService = new CharacterTransactionService(obtainCharacterTransaction,
                saveCharacterTransaction, randomString);
    }

    @Test
    void testAvailableDebt_True() {
        when(obtainCharacterTransaction.getCharacterIdTransaction(CHARACTER_ID, TRANSACTION_ID))
                .thenReturn(List.of());

        boolean result = characterTransactionService.availableDebt(CHARACTER_ID, MONEY, COST, TRANSACTION_ID);

        assertTrue(result);
    }

    @Test
    void testAvailableDebt_False() {
        CharacterTransactionEntity transaction = new CharacterTransactionEntity();
        transaction.setAmount(400.0);
        when(obtainCharacterTransaction.getCharacterIdTransaction(CHARACTER_ID, TRANSACTION_ID))
                .thenReturn(List.of(transaction));

        boolean result = characterTransactionService.availableDebt(CHARACTER_ID, MONEY, COST, TRANSACTION_ID);

        assertFalse(result);
    }


    @Test
    void testExistTransaction_True() {
        when(obtainCharacterTransaction.findByReference(REFERENCE, TRANSACTION_ID))
                .thenReturn(Optional.of(new CharacterTransactionEntity()));

        boolean result = characterTransactionService.existTransaction(REFERENCE, TRANSACTION_ID);

        assertTrue(result);
    }

    @Test
    void testExistTransaction_False() {
        when(obtainCharacterTransaction.findByReference(REFERENCE, TRANSACTION_ID))
                .thenReturn(Optional.empty());

        boolean result = characterTransactionService.existTransaction(REFERENCE, TRANSACTION_ID);

        assertFalse(result);
    }

    @Test
    void testFindByTransactionType() {
        TransactionType type = TransactionType.BANK;
        when(obtainCharacterTransaction.findByTransactionType(type.name(), TRANSACTION_ID))
                .thenReturn(List.of(new CharacterTransactionEntity()));

        List<CharacterTransactionEntity> result = characterTransactionService.findByTransactionType(type,
                TRANSACTION_ID);

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateTransaction() {
        CharacterTransactionEntity entity = new CharacterTransactionEntity();
        characterTransactionService.update(entity, TRANSACTION_ID);

        verify(saveCharacterTransaction, times(1)).save(entity, TRANSACTION_ID);
    }
}
