package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.transaction.*;
import com.auth.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    @Mock
    private TransactionPort transactionPort;

    @InjectMocks
    private TransactionController transactionController;

    private final String transactionId = "12345";
    private final String emulator = EmulatorCore.AZEROTH_CORE.getName();
    private final Long userId = 1L;
    private final Long accountId = 10L;
    private final Long characterId = 20L;

    @Test
    void testSendItems_Success() {
        CreateTransactionItemsDto request = new CreateTransactionItemsDto();
        request.setAccountId(accountId);
        request.setUserId(userId);
        request.setItems(List.of(new ItemQuantityDto("1", 2)));
        request.setReference("ref123");
        request.setAmount(100.0);

        ResponseEntity<GenericResponse<Void>> response =
                transactionController.sendItems(transactionId, emulator, request);

        verify(transactionPort, times(1)).sendItems(userId, accountId, request.getItems(),
                request.getReference(), request.getAmount(), emulator, transactionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSubscriptionBenefits_Success() {
        SubscriptionBenefitsDto request = new SubscriptionBenefitsDto();
        request.setAccountId(accountId);
        request.setCharacterId(characterId);
        request.setUserId(userId);
        request.setItems(List.of(new ItemQuantityDto("1", 3)));
        request.setBenefitType("EXP_BOOST");
        request.setAmount(50.0);
        ResponseEntity<GenericResponse<Void>> response =
                transactionController.subscriptionBenefits(transactionId, emulator, request);

        verify(transactionPort, times(1)).sendSubscriptionBenefits(userId, accountId, characterId, request.getItems(),
                request.getBenefitType(), request.getAmount(), emulator, transactionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testClaimPromotions_Success() {
        PromotionsDto request = new PromotionsDto();
        request.setAccountId(accountId);
        request.setCharacterId(characterId);
        request.setUserId(userId);
        request.setType(PromotionType.ITEM.name());
        request.setLevel(25);
        request.setMinLvl(10);
        request.setMaxLvl(50);
        request.setAmount(200.0);
        request.setItems(List.of(new ItemQuantityDto("1", 5)));
        request.setType(PromotionType.ITEM.name());

        ResponseEntity<GenericResponse<Void>> response =
                transactionController.claimPromotions(transactionId, emulator, request);

        verify(transactionPort, times(1)).sendPromotion(userId, accountId, characterId, request.getItems(),
                request.getType(), request.getAmount(), request.getMinLvl(), request.getMaxLvl(), request.getLevel(),
                emulator, transactionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testClaimGuildBenefits_Success() {
        BenefitsGuildDto request = new BenefitsGuildDto();
        request.setAccountId(accountId);
        request.setItems(List.of(new ItemQuantityDto("2", 10)));
        request.setCharacterId(characterId);
        request.setUserId(userId);

        ResponseEntity<GenericResponse<Void>> response =
                transactionController.claimGuildBenefits(transactionId, emulator, request);

        verify(transactionPort, times(1)).sendBenefitsGuild(userId, accountId, characterId, request.getItems(),
                emulator, transactionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testClaimMachine_Success() {
        MachineDto request = new MachineDto();
        request.setType(MachineType.GOLD.getName());
        request.setAccountId(accountId);
        request.setCharacterId(characterId);

        MachineClaimDto expectedClaim = new MachineClaimDto(true);

        when(transactionPort.sendMachine(accountId, characterId, request.getType(), emulator, transactionId))
                .thenReturn(expectedClaim);

        ResponseEntity<GenericResponse<MachineClaimDto>> response =
                transactionController.claimGuildBenefits(transactionId, emulator, request);

        verify(transactionPort, times(1)).sendMachine(accountId, characterId, request.getType(), emulator, transactionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedClaim, response.getBody().getData());
    }
}
