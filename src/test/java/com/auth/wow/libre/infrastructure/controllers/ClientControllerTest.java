package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.user.*;
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
class ClientControllerTest {
    @Mock
    private UserPort userPort;

    @InjectMocks
    private ClientController clientController;

    private CreateClientDto request;

    @BeforeEach
    void setUp() {
        request = new CreateClientDto();
        request.setUsername("testUser");
        request.setPassword("testPass");
        request.setSalt(new byte[]{1, 2, 3, 4});
    }

    @Test
    void testCreateClient_Success() {
        String transactionId = "12345";

        ResponseEntity<GenericResponse<Void>> response = clientController.create(transactionId, request);

        verify(userPort, times(1)).create(request.getUsername(), request.getPassword(),
                request.getSalt(), request.getGameMasterUsername(),
                request.getGameMasterPassword(), request.getEmulator(), request.getApiKey(), transactionId);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionId, Objects.requireNonNull(response.getBody()).getTransactionId());
    }
}
