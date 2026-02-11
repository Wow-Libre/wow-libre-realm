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
    public void execute(String command, EmulatorCore core, String transactionId) throws JAXBException {


        switch (core) {
            case TRINITY_CORE -> trinityClient.executeCommand(command);
            case AZEROTH_CORE -> azerothClient.executeCommand(command);
            default -> throw new InternalException("No client defined for core: " + core.getName(), transactionId);
        }
    }
}
