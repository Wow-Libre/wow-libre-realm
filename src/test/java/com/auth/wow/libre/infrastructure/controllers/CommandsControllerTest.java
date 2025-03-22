package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandsControllerTest {
    @Mock
    private ExecuteCommandsPort executeCommandsPort;

    @InjectMocks
    private CommandsController commandsController;

    @Test
    void testCommands_Success() {
        String transactionId = "12345";
        ExecuteCommandRequest request = new ExecuteCommandRequest();
        request.setMessage("test");
        request.setSalt(new byte[16]);
        ResponseEntity<GenericResponse<Void>> response = commandsController.commands(transactionId, request);

        verify(executeCommandsPort, times(1)).execute(request.getMessage(), request.getSalt(), transactionId);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
