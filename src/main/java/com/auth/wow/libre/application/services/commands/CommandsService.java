package com.auth.wow.libre.application.services.commands;

import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.in.config.*;
import com.auth.wow.libre.infrastructure.client.*;
import com.auth.wow.libre.infrastructure.util.*;
import jakarta.xml.bind.*;
import org.springframework.stereotype.*;

import javax.crypto.*;

@Service
public class CommandsService implements ExecuteCommandsPort {
    private final CoreClient azerothClient;
    private final TrinityClient trinityClient;
    private final ConfigPort configPort;


    public CommandsService(CoreClient azerothClient, TrinityClient trinityClient,
                           ConfigPort configPort) {
        this.azerothClient = azerothClient;
        this.trinityClient = trinityClient;
        this.configPort = configPort;
    }

    @Override
    public void execute(String messageEncrypt, byte[] salt, String transactionId) {
        final String apiKey = configPort.apiKey(transactionId);
        final String apiSecret = configPort.apiSecret(apiKey, transactionId);

        try {
            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret, salt);
            final String decryptedCommand = EncryptionUtil.decrypt(messageEncrypt, derivedKey);

            execute(decryptedCommand, transactionId);
        } catch (Exception e) {
            throw new InternalException("An error has occurred with encryption or the SOAP system is not available " +
                    "from the emulator", transactionId);
        }

    }

    @Override
    public void execute(String command, String transactionId) throws JAXBException {

        EmulatorCore core = configPort.emulator(transactionId);

        switch (core) {
            case TRINITY_CORE -> trinityClient.executeCommand(command);
            case AZEROTH_CORE -> azerothClient.executeCommand(command);
            default -> throw new UnsupportedOperationException("No client defined for core: " + core.getName());
        }
    }
}
