package com.auth.wow.libre.domain.strategy.accounts;

import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.domain.strategy.commands.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import jakarta.xml.bind.*;
import org.slf4j.*;

import java.util.*;


public class AccountLK extends Account {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountLK.class);

    private final ExecuteCommandsPort executeCommandsPort;
    private final ObtainAccountPort obtainAccountPort;
    private final SaveAccountPort saveAccountPort;


    public AccountLK(ExecuteCommandsPort executeCommandsPort, ObtainAccountPort obtainAccountPort,
                     SaveAccountPort saveAccountPort) {
        this.executeCommandsPort = executeCommandsPort;
        this.obtainAccountPort = obtainAccountPort;
        this.saveAccountPort = saveAccountPort;
    }

    @Override
    public void create(String username, String password, String email, Long userId, EmulatorCore emulator,
                       String transactionId) {

        final boolean usernameExist = obtainAccountPort.findByUsername(username).isPresent();

        if (usernameExist) {
            LOGGER.error("The requested username is already registered in the system {}", transactionId);
            throw new InternalException("The username is not available", transactionId);
        }

        try {
            final CommandStrategy commandStrategy = CommandStrategyFactory.getStrategy(Expansion.WRATH_OF_THE_LICH_KING,
                    emulator.getName());

            executeCommandsPort.execute(commandStrategy.getCreateCommand(username, password), transactionId);
        } catch (JAXBException e) {
            LOGGER.error("[AccountLK] [create] The client could not be registered because SOAP communication is " +
                    "unavailable or because SOAP access is invalid.");
            throw new InternalException("It was not possible to register the client to the system", transactionId);
        }

        Optional<AccountEntity> accountLk = obtainAccountPort.findByUsername(username);

        if (accountLk.isEmpty()) {
            LOGGER.error("[AccountLK] [create] An error occurred while retrieving the registered account after SOAP " +
                    "registration. transactionId {}", transactionId);
            throw new InternalException("It was not possible to register the client to the system", transactionId);
        }

        AccountEntity account = accountLk.get();
        account.setUserId(userId);
        saveAccountPort.save(account);
        setId(account.getId());
    }

    @Override
    public void changePassword(String username, String password, String email, EmulatorCore emulator,
                               String transactionId) {

        try {
            final CommandStrategy commandStrategy = CommandStrategyFactory.getStrategy(Expansion.WRATH_OF_THE_LICH_KING,
                    emulator.getName());
            executeCommandsPort.execute(commandStrategy.getChangePasswordCommand(username, password), transactionId);
        } catch (JAXBException e) {
            LOGGER.error("[AccountLK] [changePassword] It was not possible to change the client password.");
            throw new InternalException("It was not possible to register the client to the system", transactionId);
        }
    }
}
