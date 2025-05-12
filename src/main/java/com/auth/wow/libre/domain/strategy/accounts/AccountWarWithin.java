package com.auth.wow.libre.domain.strategy.accounts;

import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.domain.strategy.commands.*;
import com.auth.wow.libre.infrastructure.conf.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import jakarta.xml.bind.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class AccountWarWithin extends Account {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountWarWithin.class);

    private final ExecuteCommandsPort executeCommandsPort;
    private final ObtainAccountPort obtainAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final Configurations configurations;


    public AccountWarWithin(ExecuteCommandsPort executeCommandsPort, ObtainAccountPort obtainAccountPort,
                            SaveAccountPort saveAccountPort, Configurations configurations) {
        this.executeCommandsPort = executeCommandsPort;
        this.obtainAccountPort = obtainAccountPort;
        this.saveAccountPort = saveAccountPort;
        this.configurations = configurations;
    }

    @Override
    public void create(String username, String password, String email, Long userId, String transactionId) {
        final String usernameDomain = String.format("%s@%s", username, configurations.getDomain());
        final boolean emailExist = obtainAccountPort.findByEmail(usernameDomain).isPresent();

        if (emailExist) {
            LOGGER.error("The requested email is already registered in the system transactionId {}", transactionId);
            throw new InternalException("The email is not available", transactionId);
        }

        try {
            final CommandStrategy commandStrategy = CommandStrategyFactory.getStrategy(Expansion.WAR_WITHIN,
                    configurations.getEmulatorType());
            executeCommandsPort.execute(commandStrategy.getCreateCommand(usernameDomain, password), transactionId);
        } catch (JAXBException e) {
            LOGGER.error("[AccountWarWithin] [create] The client could not be registered because SOAP communication " +
                    "is  unavailable or because SOAP access is invalid. transactionId {}", transactionId);
            throw new InternalException("It was not possible to register the client to the system", transactionId);
        }

        Optional<AccountEntity> accountLk = obtainAccountPort.findByEmail(usernameDomain);

        if (accountLk.isEmpty()) {
            LOGGER.error("[AccountWarWithin] [create] An error occurred while retrieving the registered account after" +
                    " SOAP registration. transactionId {}", transactionId);
            throw new InternalException("It was not possible to register the client to the system", transactionId);
        }

        AccountEntity account = accountLk.get();
        account.setUserId(userId);
        saveAccountPort.save(account);
        setId(account.getId());
    }

    @Override
    public void changePassword(String username, String password, String email, String transactionId) {
        try {
            final CommandStrategy commandStrategy = CommandStrategyFactory.getStrategy(Expansion.WAR_WITHIN,
                    configurations.getEmulatorType());
            executeCommandsPort.execute(commandStrategy.getChangePasswordCommand(username, password), transactionId);
            executeCommandsPort.execute(commandStrategy.getChangePasswordBattleNetCommand(email, password),
                    transactionId);
        } catch (JAXBException e) {
            LOGGER.error("[AccountWarWithin] [changePassword] It was not possible to change the client password.");
            throw new InternalException("It was not possible to register the client to the system", transactionId);
        }
    }
}
