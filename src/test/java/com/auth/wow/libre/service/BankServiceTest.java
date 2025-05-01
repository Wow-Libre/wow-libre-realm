package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.bank.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.domain.ports.out.characters.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankServiceTest {
    @Mock
    private ObtainCharacters obtainCharacters;

    @Mock
    private SaveCharacters saveCharacters;

    @Mock
    private ObtainAccountPort obtainAccountPort;

    @InjectMocks
    private BankService bankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCollectGold_SufficientFunds() {
        Long userId = 1L;
        Double moneyToPay = 50.0;
        String transactionId = "txn123";

        AccountEntity account = new AccountEntity();
        account.setId(100L);
        account.setOnline(false);

        CharactersEntity character = new CharactersEntity();
        character.setMoney(100.0);

        when(obtainAccountPort.findByUserId(userId)).thenReturn(List.of(account));
        when(obtainCharacters.getCharactersAndAccountId(account.getId(), transactionId)).thenReturn(List.of(character));

        Double remaining = bankService.collectGold(userId, moneyToPay, transactionId);

        assertEquals(0.0, remaining);
        assertEquals(50.0, character.getMoney());
        verify(saveCharacters, times(1)).save(character, transactionId);
    }

    @Test
    void testCollectGold_InsufficientFunds() {
        Long userId = 1L;
        Double moneyToPay = 200.0;
        String transactionId = "txn123";

        AccountEntity account = new AccountEntity();
        account.setId(100L);
        account.setOnline(false);

        CharactersEntity character = new CharactersEntity();
        character.setMoney(100.0);

        when(obtainAccountPort.findByUserId(userId)).thenReturn(List.of(account));
        when(obtainCharacters.getCharactersAndAccountId(account.getId(), transactionId)).thenReturn(List.of(character));

        Double remaining = bankService.collectGold(userId, moneyToPay, transactionId);

        assertEquals(100.0, remaining);
        assertEquals(0.0, character.getMoney());
        verify(saveCharacters, times(1)).save(character, transactionId);
    }

    @Test
    void testCollectGold_NoAccounts() {
        Long userId = 1L;
        Double moneyToPay = 50.0;
        String transactionId = "txn123";

        when(obtainAccountPort.findByUserId(userId)).thenReturn(List.of());

        Double remaining = bankService.collectGold(userId, moneyToPay, transactionId);

        assertEquals(50.0, remaining);
        verifyNoInteractions(obtainCharacters);
        verifyNoInteractions(saveCharacters);
    }

    @Test
    void testCollectGold_NoValidCharacters() {
        Long userId = 1L;
        Double moneyToPay = 50.0;
        String transactionId = "txn123";

        AccountEntity account = new AccountEntity();
        account.setId(100L);
        account.setOnline(false);

        when(obtainAccountPort.findByUserId(userId)).thenReturn(List.of(account));
        when(obtainCharacters.getCharactersAndAccountId(account.getId(), transactionId)).thenReturn(List.of());

        Double remaining = bankService.collectGold(userId, moneyToPay, transactionId);

        assertEquals(50.0, remaining);
        verify(saveCharacters, never()).save(any(), any());
    }

    @Test
    void testCollectGold_StopAfterPaymentSatisfied() {
        Long userId = 1L;
        Double moneyToPay = 100.0;
        String transactionId = "txn123";

        AccountEntity account = new AccountEntity();
        account.setId(100L);
        account.setOnline(false);

        CharactersEntity character1 = new CharactersEntity();
        character1.setMoney(60.0);

        CharactersEntity character2 = new CharactersEntity();
        character2.setMoney(100.0);

        when(obtainAccountPort.findByUserId(userId)).thenReturn(List.of(account));
        when(obtainCharacters.getCharactersAndAccountId(account.getId(), transactionId))
                .thenReturn(List.of(character1, character2));

        Double remaining = bankService.collectGold(userId, moneyToPay, transactionId);

        // Se deducen 60 de character1 y 40 de character2
        assertEquals(-60.0, remaining);
        assertEquals(0.0, character1.getMoney());
        assertEquals(0.0, character2.getMoney());

        // Verificamos que solo se guardan los que se usan
        verify(saveCharacters, times(2)).save(any(), eq(transactionId));
    }

    @Test
    void testCollectGold_MultipleAccounts() {
        Long userId = 1L;
        Double moneyToPay = 100.0;
        String transactionId = "txn123";

        AccountEntity account1 = new AccountEntity();
        account1.setId(101L);
        account1.setOnline(false);

        AccountEntity account2 = new AccountEntity();
        account2.setId(102L);
        account2.setOnline(false);

        CharactersEntity character1 = new CharactersEntity();
        character1.setMoney(30.0);

        CharactersEntity character2 = new CharactersEntity();
        character2.setMoney(80.0);

        when(obtainAccountPort.findByUserId(userId)).thenReturn(List.of(account1, account2));
        when(obtainCharacters.getCharactersAndAccountId(account1.getId(), transactionId)).thenReturn(List.of(character1));
        when(obtainCharacters.getCharactersAndAccountId(account2.getId(), transactionId)).thenReturn(List.of(character2));

        Double remaining = bankService.collectGold(userId, moneyToPay, transactionId);

        assertEquals(-10.0, remaining);
        assertEquals(0.0, character1.getMoney());
        assertEquals(0.0, character2.getMoney());

        verify(saveCharacters, times(2)).save(any(), eq(transactionId));
    }

}
