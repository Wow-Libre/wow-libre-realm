package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.application.services.encryption.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.account_banned.*;
import com.auth.wow.libre.domain.ports.in.google.*;
import com.auth.wow.libre.domain.ports.in.wow_libre.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account.*;
import com.auth.wow.libre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import javax.crypto.*;
import java.security.*;
import java.time.*;
import java.util.*;

@Service
public class AccountService implements AccountPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    public static final String EXPANSION_LK = "2";

    private final ObtainAccountPort obtainAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final AccountBannedPort accountBannedPort;
    private final WowLibrePort wowLibrePort;
    private final GooglePort googlePort;
    private final SaveBannedPort saveBannedPort;


    public AccountService(ObtainAccountPort obtainAccountPort,
                          SaveAccountPort saveAccountPort,
                          AccountBannedPort accountBannedPort,
                          WowLibrePort wowLibrePort, GooglePort googlePort, SaveBannedPort saveBannedPort) {
        this.obtainAccountPort = obtainAccountPort;
        this.saveAccountPort = saveAccountPort;
        this.accountBannedPort = accountBannedPort;
        this.wowLibrePort = wowLibrePort;
        this.googlePort = googlePort;
        this.saveBannedPort = saveBannedPort;
    }

    @Override
    public Long create(String username, String password, String email,
                       Long userId, String expansion, byte[] saltPassword,
                       String transactionId) {

        final String jwt = wowLibrePort.getJwt(transactionId);
        final ServerKey apiSecret = wowLibrePort.getApiSecret(jwt, transactionId);

        try {
            final SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret.keyPassword(), saltPassword);
            final String decryptedPassword = EncryptionUtil.decrypt(password, derivedKey);

            final boolean usernameExist = obtainAccountPort.findByUsername(username).isPresent();

            if (usernameExist) {
                LOGGER.error("The requested username is already registered in the system {}", transactionId);
                throw new InternalException("The username is not available", transactionId);
            }

            SecureRandom random = new SecureRandom();

            byte[] salt = new byte[32];
            random.nextBytes(salt);

            byte[] verifier = getVerifier(username, salt, decryptedPassword);

            AccountEntity account = new AccountEntity();
            account.setSalt(salt);
            account.setVerifier(verifier);
            account.setLocked(false);
            account.setUsername(username);
            account.setEmail(email);
            account.setExpansion(expansion);
            account.setUserId(userId);
            return saveAccountPort.save(account).getId();
        } catch (InternalException e) {
            throw new InternalException(e.getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[AccountService] [create] It was not possible to create the account on the server, there " +
                            "was a failure with the " +
                            "encryption. {} {}",
                    transactionId, e.getMessage());
            throw new InternalException(
                    "It was not possible to create the client, please try later and contact support", transactionId);
        }


    }

    private byte[] getVerifier(String username, byte[] salt, String decryptedPassword) throws Exception {
        return EncryptionService.computeVerifier(ParamsEncrypt.trinitycore, salt, username.toUpperCase(),
                decryptedPassword.toUpperCase());
    }

    @Override
    public void createUser(String username, String password, String email, String recaptchaToken,
                           String ipAddress, String transactionId) {

        if (!googlePort.verifyRecaptcha(recaptchaToken, ipAddress).getSuccess()) {
            LOGGER.error("The captcha is invalid");
            throw new InternalException("The captcha is invalid", transactionId);
        }

        final boolean usernameExist = obtainAccountPort.findByUsername(username).isPresent();

        if (usernameExist) {
            LOGGER.error("The username is not available - transactionId {}", transactionId);
            throw new InternalException("The username is not available", transactionId);
        }

        try {
            byte[] salt = new byte[32];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            byte[] verifier = getVerifier(username.toUpperCase(), salt, password.toUpperCase());

            AccountEntity account = new AccountEntity();
            account.setSalt(salt);
            account.setVerifier(verifier);
            account.setLocked(false);
            account.setUsername(username);
            account.setEmail(email);
            account.setExpansion(EXPANSION_LK);
            account.setUserId(null);
            saveAccountPort.save(account);
        } catch (Exception e) {
            LOGGER.error("It was not possible to create the user, an unexpected error occurred {}", e.getMessage());
            throw new InternalException("It was not possible to create the user, an unexpected error occurred", "");
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
            LOGGER.error("The requested account is not linked to the user {} or the account id {} " +
                    "- TransactionId {}", accountId, userId, transactionId);
            throw new InternalException("The requested account is not linked to the user or the account id",
                    transactionId);
        }

        final String jwt = wowLibrePort.getJwt(transactionId);
        final ServerKey apiSecret = wowLibrePort.getApiSecret(jwt, transactionId);

        try {
            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret.keyPassword(), saltPassword);
            final String decryptedPassword = EncryptionUtil.decrypt(password, derivedKey);
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[32];
            random.nextBytes(salt);

            AccountEntity accountUpdate = account.get();
            accountUpdate.setSalt(salt);

            byte[] verifier = getVerifier(accountUpdate.getUsername(), salt, decryptedPassword);


            accountUpdate.setVerifier(verifier);

            saveAccountPort.save(accountUpdate);
        } catch (Exception e) {
            LOGGER.error("[AccountService][changePassword] Could not update password: {} {}", e.getMessage(),
                    transactionId, e);
            throw new InternalException("Could not update password", transactionId);
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
                        account.getLastLogin(), account.getOs(),
                        getAccountBanned(account) != null)).toList(),
                obtainAccountPort.count());
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
        }).orElseThrow(() -> new InternalException("Cannot find a user" +
                " with that username", transactionId));
    }

    @Override
    public void bannedUser(String username, Integer days, Integer hours, Integer minutes, Integer seconds,
                           String bannedBy, String banReason, String transactionId) {

        final Optional<AccountEntity> accountModel = obtainAccountPort.findByUsername(username);

        if (accountModel.isEmpty()) {
            LOGGER.error("The server where your character is " + "currently located is not available {}",
                    transactionId);
            throw new InternalException("The server where your character is " +
                    "currently located is not available", transactionId);
        }

        AccountEntity account = accountModel.get();

        if (getAccountBanned(account) != null
                && accountBannedPort.getAccountBanned(account.getId()).active) {
            LOGGER.error("The client already submits a ban {} AccountId: {}", account.getId(), transactionId);
            throw new InternalException("The client already submits a ban", transactionId);
        }

        long banDate = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long unBanDate = banDate + (days * 86400L) + (hours * 3600L) + (minutes * 60L) + seconds;

        saveBannedPort.save(account.getId(), banDate, unBanDate, bannedBy, banReason, true);
    }

    @Override
    @Transactional
    public void changePassword(String username, String password, String newPassword, String transactionId) {
        final Optional<AccountEntity> account = obtainAccountPort.findByUsername(username);

        if (account.isEmpty()) {
            LOGGER.error("The requested account is not linked to the user {}  TransactionId {}", username,
                    transactionId);
            throw new InternalException("The requested account is not linked to the user or the account id",
                    transactionId);
        }

        try {
            byte[] salt = new byte[32];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            byte[] verifier = getVerifier(account.get().getUsername(), salt, newPassword);

            AccountEntity accountUpdate = account.get();
            accountUpdate.setSalt(salt);
            accountUpdate.setVerifier(verifier);

            saveAccountPort.save(accountUpdate);
        } catch (Exception e) {
            LOGGER.error("[AccountService][changePassword] Could not update password: {} {}", e.getMessage(),
                    transactionId, e);
            throw new InternalException("Could not update password", transactionId);
        }
    }
}
