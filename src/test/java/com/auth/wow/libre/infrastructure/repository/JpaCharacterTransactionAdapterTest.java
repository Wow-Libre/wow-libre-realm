package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.character_transaction.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaCharacterTransactionAdapterTest {
    @Mock
    private CharacterTransactionRepository characterTransactionRepository;

    @InjectMocks
    private JpaCharacterTransactionAdapter jpaCharacterTransactionAdapter;

    private CharacterTransactionEntity transactionEntity;
    private static final Long CHARACTER_ID = 1L;
    private static final String TRANSACTION_ID = "txn-123";
    private static final String REFERENCE = "ref-456";
    private static final String TRANSACTION_TYPE = "payment";

    @BeforeEach
    void setUp() {
        transactionEntity = new CharacterTransactionEntity();
    }

    @Test
    void testSave() {
        jpaCharacterTransactionAdapter.save(transactionEntity, TRANSACTION_ID);
        verify(characterTransactionRepository, times(1)).save(transactionEntity);
    }

    @Test
    void testGetCharacterIdTransaction() {
        List<CharacterTransactionEntity> expectedTransactions = List.of(transactionEntity);
        when(characterTransactionRepository.findByCharacterIdAndStatusIsTrueAndIndebtednessIsTrue(CHARACTER_ID))
                .thenReturn(expectedTransactions);

        List<CharacterTransactionEntity> result =
                jpaCharacterTransactionAdapter.getCharacterIdTransaction(CHARACTER_ID, TRANSACTION_ID);

        assertFalse(result.isEmpty());
        assertEquals(expectedTransactions, result);
        verify(characterTransactionRepository, times(1)).findByCharacterIdAndStatusIsTrueAndIndebtednessIsTrue(CHARACTER_ID);
    }

    @Test
    void testFindByReference() {
        when(characterTransactionRepository.findByReference(REFERENCE)).thenReturn(Optional.of(transactionEntity));

        Optional<CharacterTransactionEntity> result = jpaCharacterTransactionAdapter.findByReference(REFERENCE,
                TRANSACTION_ID);

        assertTrue(result.isPresent());
        assertEquals(transactionEntity, result.get());
        verify(characterTransactionRepository, times(1)).findByReference(REFERENCE);
    }

    @Test
    void testFindByTransactionType() {
        List<CharacterTransactionEntity> expectedTransactions = List.of(transactionEntity);
        when(characterTransactionRepository.findByTransactionTypeAndStatusIsTrue(TRANSACTION_TYPE))
                .thenReturn(expectedTransactions);

        List<CharacterTransactionEntity> result =
                jpaCharacterTransactionAdapter.findByTransactionType(TRANSACTION_TYPE, TRANSACTION_ID);

        assertFalse(result.isEmpty());
        assertEquals(expectedTransactions, result);
        verify(characterTransactionRepository, times(1)).findByTransactionTypeAndStatusIsTrue(TRANSACTION_TYPE);
    }
}
