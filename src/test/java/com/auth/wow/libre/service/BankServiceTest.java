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
}
