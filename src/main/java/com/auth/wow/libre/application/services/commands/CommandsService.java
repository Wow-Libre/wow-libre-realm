package com.auth.wow.libre.application.services.commands;

import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.infrastructure.client.*;
import jakarta.xml.bind.*;
import org.springframework.stereotype.*;

@Service
public class CommandsService implements ExecuteCommandsPort {
    private final CoreClient azerothClient;
    private final TrinityClient trinityClient;


    public CommandsService(CoreClient azerothClient, TrinityClient trinityClient) {
        this.azerothClient = azerothClient;
        this.trinityClient = trinityClient;
    }

    @Override
    public void execute(String messageEncrypt, byte[] salt, String transactionId) {

        try {
            execute(messageEncrypt, transactionId);
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
