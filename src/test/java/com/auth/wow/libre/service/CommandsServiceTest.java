package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.commands.*;
import com.auth.wow.libre.infrastructure.client.*;
import jakarta.xml.bind.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandsServiceTest {
    @Mock
    private CoreClient coreClient;

    @InjectMocks
    private CommandsService commandsService;

    private static final String TRANSACTION_ID = "txn-1234";


    @Test
    void execute_withPlainCommand_shouldExecuteSuccessfully() throws JAXBException {
        String plainCommand = "some-command";
        commandsService.execute(plainCommand, TRANSACTION_ID);
        verify(coreClient, times(1)).executeCommand(plainCommand);
    }
}

