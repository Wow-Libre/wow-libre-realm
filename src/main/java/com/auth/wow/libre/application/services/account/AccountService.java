package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.account_banned.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.domain.strategy.accounts.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class AccountService implements AccountPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final ObtainAccountPort obtainAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final AccountBannedPort accountBannedPort;
    private final SaveBannedPort saveBannedPort;
    private final ExecuteCommandsPort executeCommandsPort;

    public AccountService(ObtainAccountPort obtainAccountPort, SaveAccountPort saveAccountPort,
                          AccountBannedPort accountBannedPort, SaveBannedPort saveBannedPort,
                          ExecuteCommandsPort executeCommandsPort) {
        this.obtainAccountPort = obtainAccountPort;
        this.saveAccountPort = saveAccountPort;
        this.accountBannedPort = accountBannedPort;
        this.saveBannedPort = saveBannedPort;
        this.executeCommandsPort = executeCommandsPort;
    }

    @Override
    public Long create(String username, String password, String email, Long userId, Integer expansionId,
                       String emulator, String transactionId) {

        final EmulatorCore emulatorCore = EmulatorCore.valueOf(emulator);

        try {

            Account account = RegisterFactory.getExpansion(expansionId, executeCommandsPort, obtainAccountPort,
                    saveAccountPort);
            account.create(username, password, email, userId, emulatorCore, transactionId);
            return account.getId();
        } catch (InternalException e) {
            throw new InternalException(e.getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[AccountService] [create] It was not possible to create the account on the server, there " + "was a failure with the encryption. TransactionId{} error: {}", transactionId, e.getMessage());
            throw new InternalException("It was not possible to create the client, please try later and contact " +
                    "support", transactionId);
        }
    }

    @Override
    public Long countOnline(String transactionId) {
        return obtainAccountPort.countOnline(transactionId);
    }

    @Override
    public Boolean isOnline(Long accountId, String transactionId) {
        return obtainAccountPort.findById(accountId).map(AccountEntity::isOnline).orElse(null);
    }

    @Override
    public AccountDetailDto account(Long accountId, String transactionId) {

        return obtainAccountPort.findById(accountId).map(account -> new AccountDetailDto(account.getId(),
                account.getUsername(), account.getEmail(), account.getExpansion(), account.isOnline(),
                account.getFailedLogins(), account.getJoinDate(), account.getLastIp(), account.getMuteReason(),
                account.getMuteBy(), account.getMuteTime() != null && account.getMuteTime() > 0,
                account.getLastLogin(), account.getOs(), accountBannedPort.getAccountBanned(accountId))).orElseThrow(() -> new NotFoundException("There is no associated account or it is not available.", transactionId));
    }

    @Override
    public void changePassword(Long accountId, Long userId, String password, Integer expansionId,
                               String emulator, String transactionId) {

        final Optional<AccountEntity> account = obtainAccountPort.findByIdAndUserId(accountId, userId);

        if (account.isEmpty()) {
            LOGGER.error("The requested account is not linked to the user {} or the account id {} " + "- " +
                    "TransactionId {}", accountId, userId, transactionId);
            throw new InternalException("The requested account is not linked to the user or the account id",
                    transactionId);
        }

        final String email = account.get().getEmail();
        final String username = account.get().getUsername();
        final EmulatorCore emulatorCore = EmulatorCore.valueOf(emulator);

        try {
            Account accountChangePassword = RegisterFactory.getExpansion(expansionId, executeCommandsPort,
                    obtainAccountPort, saveAccountPort);
            accountChangePassword.changePassword(username, password, email, emulatorCore, transactionId);
        } catch (Exception e) {
            LOGGER.error("[AccountService][changePassword] Could not update password: {} {}", e.getMessage(),
                    transactionId, e);
            throw new InternalException("Could not update password", transactionId);
        }
    }

    @Override
    public AccountsDto accounts(int size, int page, String filter, String transactionId) {
        return new AccountsDto(obtainAccountPort.findByAll(size, page, filter).stream().map(account -> new AccountsServerDto(account.getId(), account.getUsername(), account.getEmail(), account.getExpansion(), account.isOnline(), account.getFailedLogins(), account.getJoinDate(), account.getLastIp(), account.getMuteReason(), account.getMuteBy(), account.getMuteTime() != null && account.getMuteTime() > 0, account.getLastLogin(), account.getOs(), getAccountBanned(account) != null)).toList(), obtainAccountPort.count());
    }

    private AccountBanned getAccountBanned(AccountEntity account) {
        return accountBannedPort.getAccountBanned(account.getId());
    }

    @Override
    public Long count(String transactionId) {
        return obtainAccountPort.count();
    }

    @Override
    public MetricsProjection metrics(String transactionId) {
        return obtainAccountPort.metrics(transactionId);
    }

    @Override
    public void updateMail(String username, String updateMail, String transactionId) {
        obtainAccountPort.findByUsername(username).map(user -> {
            user.setEmail(updateMail);
            saveAccountPort.save(user);
            return user;
        }).orElseThrow(() -> new InternalException("Cannot find a user" + " with that username", transactionId));
    }

    @Override
    public void bannedUser(String username, Integer days, Integer hours, Integer minutes, Integer seconds,
                           String bannedBy, String banReason, String transactionId) {

        final Optional<AccountEntity> accountModel = obtainAccountPort.findByUsername(username);

        if (accountModel.isEmpty()) {
            LOGGER.error("The server where your character is " + "currently located is not available {}",
                    transactionId);
            throw new InternalException("The server where your character is " + "currently located is not available",
                    transactionId);
        }

        AccountEntity account = accountModel.get();

        if (getAccountBanned(account) != null && accountBannedPort.getAccountBanned(account.getId()).active) {
            LOGGER.error("The client already submits a ban {} AccountId: {}", account.getId(), transactionId);
            throw new InternalException("The client already submits a ban", transactionId);
        }

        long banDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long unBanDate = banDate + (days * 86400L) + (hours * 3600L) + (minutes * 60L) + seconds;

        saveBannedPort.save(account.getId(), banDate, unBanDate, bannedBy, banReason, true);
    }


}
