package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.bank.*;
import com.auth.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankControllerTest {
    @Mock
    private BankPort bankPort;

    @InjectMocks
    private BankController bankController;

    private BankPaymentDto bankPaymentDto;

    @BeforeEach
    void setUp() {
        bankPaymentDto = new BankPaymentDto();
        bankPaymentDto.setUserId(1L);
        bankPaymentDto.setAmount(100.0);
    }

    @Test
    void testPayment_Success() {
        String transactionId = "12345";
        Double remainingAmount = 50.0;

        when(bankPort.collectGold(bankPaymentDto.getUserId(), bankPaymentDto.getAmount(), transactionId))
                .thenReturn(remainingAmount);

        ResponseEntity<GenericResponse<Double>> response =
                bankController.payment(transactionId, bankPaymentDto);

        verify(bankPort, times(1)).collectGold(bankPaymentDto.getUserId(), bankPaymentDto.getAmount(), transactionId);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(remainingAmount, response.getBody().getData());
    }
}
