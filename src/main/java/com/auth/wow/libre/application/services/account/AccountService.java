package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.application.services.encryption.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.wow_libre.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.security.*;

@Service
public class AccountService implements AccountPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final ObtainAccountPort obtainAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final RandomString randomString;

    private final WowLibrePort wowLibrePort;

    public AccountService(ObtainAccountPort obtainAccountPort,
                          SaveAccountPort saveAccountPort,
                          @Qualifier("random-username") RandomString randomString,
                          WowLibrePort wowLibrePort) {
        this.obtainAccountPort = obtainAccountPort;
        this.saveAccountPort = saveAccountPort;
        this.randomString = randomString;
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
            throw new InternalException(
                    transactionId, "The account could not be created, something has failed in the encryption");
        } catch (Exception e) {
            LOGGER.error("An error occurred during processing: {}", e.getMessage(), e);
            throw new InternalException(
                    transactionId, "It was not possible to create the client, please try later and contact support");
        }


    }

    @Override
    public Boolean isOnline(Long accountId, String transactionId) {
        return obtainAccountPort.findById(accountId).map(AccountEntity::isOnline).orElse(null);
    }

}
