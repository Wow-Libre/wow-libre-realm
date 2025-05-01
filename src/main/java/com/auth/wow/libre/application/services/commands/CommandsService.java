package com.auth.wow.libre.application.services.commands;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.in.wow_libre.*;
import com.auth.wow.libre.infrastructure.client.*;
import com.auth.wow.libre.infrastructure.conf.*;
import com.auth.wow.libre.infrastructure.util.*;
import jakarta.xml.bind.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.util.*;

@Service
public class CommandsService implements ExecuteCommandsPort {
    private final CoreClient azerothClient;
    private final WowLibrePort wowLibrePort;
    private final TrinityClient trinityClient;
    private final Configurations configurations;

    public CommandsService(CoreClient azerothClient, WowLibrePort wowLibrePort, TrinityClient trinityClient,
                           Configurations configurations) {
        this.azerothClient = azerothClient;
        this.wowLibrePort = wowLibrePort;
        this.trinityClient = trinityClient;
        this.configurations = configurations;
    }

    @Override
    public void execute(String messageEncrypt, byte[] salt, String transactionId) {

        final String jwt = wowLibrePort.getJwt(transactionId);
        final ServerKey apiSecret = wowLibrePort.getApiSecret(jwt, transactionId);

        try {
            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret.keyPassword(), salt);
            final String decryptedCommand = EncryptionUtil.decrypt(messageEncrypt, derivedKey);

            execute(decryptedCommand, transactionId);
        } catch (Exception e) {
            throw new InternalException("An error has occurred with encryption or the SOAP system is not available " +
                    "from the emulator", transactionId);
        }

    }

    @Override
    public void execute(String command, String transactionId) throws JAXBException {

        EmulatorCore core = Arrays.stream(EmulatorCore.values())
                .filter(c -> c.getName().equalsIgnoreCase(configurations.getEmulatorType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported emulator core: " + configurations.getEmulatorType()));

        switch (core) {
            case TRINITY_CORE -> trinityClient.executeCommand(command);
            case AZEROTH_CORE -> azerothClient.executeCommand(command);
            default -> throw new UnsupportedOperationException("No client defined for core: " + core.getName());
        }
    }
}
