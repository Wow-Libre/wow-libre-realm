package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.application.services.encryption.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.account_banned.*;
import com.auth.wow.libre.domain.ports.in.wow_libre.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account.*;
import com.auth.wow.libre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.security.*;
import java.util.*;

@Service
public class AccountService implements AccountPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final ObtainAccountPort obtainAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final AccountBannedPort accountBannedPort;
    private final WowLibrePort wowLibrePort;

    public AccountService(ObtainAccountPort obtainAccountPort,
                          SaveAccountPort saveAccountPort,
                          AccountBannedPort accountBannedPort,
                          WowLibrePort wowLibrePort) {
        this.obtainAccountPort = obtainAccountPort;
        this.saveAccountPort = saveAccountPort;
        this.accountBannedPort = accountBannedPort;
        this.wowLibrePort = wowLibrePort;
    }

    @Override
    public Long create(String username, String password, String email,
                       Long userId, String expansion, byte[] saltPassword,
                       String transactionId) {

        final String jwt = wowLibrePort.getJwt(transactionId);
        final ServerModel apiSecret = wowLibrePort.apiSecret(jwt, transactionId);

        try {
            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret.keyPassword, saltPassword);
            final String decryptedPassword = EncryptionUtil.decrypt(password, derivedKey);

            boolean usernameExist = obtainAccountPort.findByUsername(username).isPresent();

            if (usernameExist) {
                LOGGER.error("The username is not available {}", transactionId);
                throw new InternalException("The username is not available", transactionId);
            }

            SecureRandom random = new SecureRandom();

            byte[] salt = new byte[32];
            random.nextBytes(salt);

            byte[] verifier = EncryptionService.computeVerifier(ParamsEncrypt.trinitycore, salt, username.toUpperCase(),
                    decryptedPassword.toUpperCase());

            AccountEntity account = new AccountEntity();
            account.setSalt(salt);
            account.setVerifier(verifier);
            account.setLocked(false);
            account.setUsername(username);
            account.setEmail(email);
            account.setExpansion(expansion);
            account.setUserId(userId);
            return saveAccountPort.save(account).getId();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("The server where your character is " + "currently located is not available {}",
                    transactionId);
            throw new InternalException(
                    "The account could not be created, something has failed in the encryption  {}",
                    transactionId);
        } catch (InternalException e) {
            throw new InternalException(e.getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("An error occurred during processing: {} {}", e.getMessage(), transactionId, e);
            throw new InternalException(
                    "It was not possible to create the client, please try later and contact support", transactionId);
        }


    }

    @Override
    public Boolean isOnline(Long accountId, String transactionId) {
        return obtainAccountPort.findById(accountId).map(AccountEntity::isOnline).orElse(null);
    }

    @Override
    public AccountDetailDto account(Long accountId, String transactionId) {

        return obtainAccountPort.findById(accountId).map(account ->
                new AccountDetailDto(account.getId(), account.getUsername(), account.getEmail(),
                        account.getExpansion(), account.isOnline(), account.getFailedLogins(),
                        account.getJoinDate(),
                        account.getLastIp(), account.getMuteReason(), account.getMuteBy(),
                        account.getMuteTime() != null && account.getMuteTime() > 0,
                        account.getLastLogin(), account.getOs(),
                        accountBannedPort.getAccountBanned(accountId))
        ).orElseThrow(() -> new NotFoundException("There is no associated account or it is not available.",
                transactionId));
    }

    @Override
    public void changePassword(Long accountId, Long userId, String password, byte[] saltPassword,
                               String transactionId) {

        final Optional<AccountEntity> account = obtainAccountPort.findByIdAndUserId(accountId, userId);

        if (account.isEmpty()) {
            LOGGER.error("The server where your character is " + "currently located is not available {}",
                    transactionId);
            throw new InternalException("The server where your character is " +
                    "currently located is not available", transactionId);
        }

        final String jwt = wowLibrePort.getJwt(transactionId);
        final ServerModel apiSecret = wowLibrePort.apiSecret(jwt, transactionId);

        try {
            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret.keyPassword, saltPassword);
            final String decryptedPassword = EncryptionUtil.decrypt(password, derivedKey);
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[32];
            random.nextBytes(salt);

            AccountEntity accountUpdate = account.get();
            accountUpdate.setSalt(salt);

            byte[] verifier = EncryptionService.computeVerifier(ParamsEncrypt.trinitycore, salt,
                    accountUpdate.getUsername().toUpperCase(),
                    decryptedPassword.toUpperCase());


            accountUpdate.setVerifier(verifier);

            saveAccountPort.save(accountUpdate);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("[AccountService][changePassword] The account could not be created, something has " +
                    "failed in the encryption {}", transactionId);
            throw new InternalException(
                    transactionId, "[AccountService][changePassword] The account could not be created, something has " +
                    "failed in the encryption");
        } catch (Exception e) {
            LOGGER.error("[AccountService][changePassword] Could not update password: {} {}", e.getMessage(),
                    transactionId, e);
            throw new InternalException(
                    transactionId, "Could not update password");
        }


    }

    @Override
    public AccountsDto accounts(int size, int page, String filter, String transactionId) {
        return new AccountsDto(obtainAccountPort.findByAll(size, page, filter).stream().map(account ->
                new AccountsServerDto(account.getId(), account.getUsername(), account.getEmail(),
                        account.getExpansion(), account.isOnline(), account.getFailedLogins(),
                        account.getJoinDate(),
                        account.getLastIp(), account.getMuteReason(), account.getMuteBy(),
                        account.getMuteTime() != null && account.getMuteTime() > 0,
                        account.getLastLogin(), account.getOs())).toList(), obtainAccountPort.count());
    }

    @Override
    public Long count(String transactionId) {
        return obtainAccountPort.count();
    }

    @Override
    public Long online(String transactionId) {
        return obtainAccountPort.countOnline(transactionId);
    }

    @Override
    public Long countUserId(String transactionId) {
        return null;
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
        }).orElseThrow(() -> new InternalException("Cannot find a user" +
                " with that username", transactionId));
    }


}
