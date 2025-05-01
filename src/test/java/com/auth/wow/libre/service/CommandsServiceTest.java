package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.commands.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.in.wow_libre.*;
import com.auth.wow.libre.infrastructure.client.*;
import com.auth.wow.libre.infrastructure.conf.*;
import com.auth.wow.libre.infrastructure.util.*;
import jakarta.xml.bind.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import javax.crypto.*;

import static com.auth.wow.libre.domain.model.enums.EmulatorCore.AZEROTH_CORE;
import static com.auth.wow.libre.domain.model.enums.EmulatorCore.TRINITY_CORE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandsServiceTest {
    @Mock
    private CoreClient azerothClient;
    @Mock
    private TrinityClient trinityClient;
    @Mock
    private WowLibrePort wowLibrePort;
    @Mock
    private Configurations configurations;

    @InjectMocks
    private CommandsService commandsService;

    private static final String TRANSACTION_ID = "txn-1234";


    @Test
    void execute_withPlainCommand_TrinityCore_shouldCallTrinityClient() throws JAXBException {
        String plainCommand = "some-command";
        when(configurations.getEmulatorType()).thenReturn(TRINITY_CORE.getName());

        commandsService.execute(plainCommand, TRANSACTION_ID);

        verify(trinityClient, times(1)).executeCommand(plainCommand);
        verify(azerothClient, never()).executeCommand(any());
    }

    @Test
    void execute_withPlainCommand_AzerothCore_shouldCallAzerothClient() throws JAXBException {
        String plainCommand = "another-command";
        when(configurations.getEmulatorType()).thenReturn(AZEROTH_CORE.getName());

        commandsService.execute(plainCommand, TRANSACTION_ID);

        verify(azerothClient, times(1)).executeCommand(plainCommand);
        verify(trinityClient, never()).executeCommand(any());
    }

    @Test
    void execute_withEncryptedCommand_shouldDecryptAndExecute() throws Exception {
        // Arrange
        String originalCommand = "gm fly on";
        byte[] salt = KeyDerivationUtil.generateSalt();

        // Esta es la contraseña usada para derivar la clave y debe coincidir con la que se usa en el mock
        String keyPassword = "super-secret";
        SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(keyPassword, salt);
        when(configurations.getEmulatorType()).thenReturn(AZEROTH_CORE.getName());

        // Cifra el comando con la clave derivada
        String encryptedCommand = EncryptionUtil.encrypt(originalCommand, derivedKey);

        // Prepara los mocks
        String jwtToken = "jwt-token";
        when(wowLibrePort.getJwt(TRANSACTION_ID)).thenReturn(jwtToken);
        when(wowLibrePort.getApiSecret(jwtToken, TRANSACTION_ID)).thenReturn(new ServerKey(keyPassword));

        // Act
        commandsService.execute(encryptedCommand, salt, TRANSACTION_ID);

        // Assert
        verify(azerothClient, times(1)).executeCommand(originalCommand);
    }


    @Test
    void execute_withUnknownCore_shouldThrowException() {
        when(configurations.getEmulatorType()).thenReturn("UNKNOWN_CORE");

        assertThrows(IllegalArgumentException.class, () ->
                commandsService.execute("cmd", TRANSACTION_ID));
    }

    @Test
    void execute_plainCommand_shouldNotUseJwtOrSecrets() throws JAXBException {
        String plainCommand = "some-command";
        when(configurations.getEmulatorType()).thenReturn(TRINITY_CORE.getName());

        commandsService.execute(plainCommand, TRANSACTION_ID);

        verify(wowLibrePort, never()).getJwt(any());
        verify(wowLibrePort, never()).getApiSecret(any(), any());
    }

    @Test
    void execute_withUnmappedCore_shouldThrowUnsupportedOperationException() {
        when(configurations.getEmulatorType()).thenReturn("Mangos"); // debe coincidir con el nombre del enum

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () ->
                commandsService.execute("some-command", TRANSACTION_ID));

        assertTrue(exception.getMessage().contains("No client defined for core: Mangos"));
    }
}

