package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.application.services.encryption.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.account_banned.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.infrastructure.entities.*;
import org.slf4j.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.security.*;

@Service
public class AccountService implements AccountPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private static final int LIMIT_ACCOUNT = 10;
    private final ObtainAccountPort obtainAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final AccountBannedPort accountBannedPort;
    private final PasswordEncoder passwordEncoder;


    public AccountService(PasswordEncoder passwordEncoder,
                          ObtainAccountPort obtainAccountPort,
                          SaveAccountPort saveAccountPort, AccountBannedPort accountBannedPort) {
        this.passwordEncoder = passwordEncoder;
        this.obtainAccountPort = obtainAccountPort;
        this.saveAccountPort = saveAccountPort;
        this.accountBannedPort = accountBannedPort;
    }

    @Override
    public void create(String username, String password, String email, String apiKey, boolean rebuildUsername,
                       Long userId,
                       String transactionId) {

        boolean usernameExist = obtainAccountPort.findByUsername(username).isPresent();

        if (usernameExist && !rebuildUsername) {
            throw new InternalException("The username is not available", transactionId);
        }

        if (usernameExist) {
            username = String.format("%s", username + "xx");
        }


        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[32];
            random.nextBytes(salt);
            ParamsEncrypt params = ParamsEncrypt.trinitycore;
            byte[] verifier = EncryptionService.computeVerifier(params, salt, username.toUpperCase(),
                    password.toUpperCase());

            AccountEntity account = new AccountEntity();
            account.setSalt(salt);
            account.setVerifier(verifier);
            account.setLocked(false);
            account.setUsername(username);
            account.setEmail(email);
            account.setExpansion("2");
            saveAccountPort.save(account);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalException(
                    transactionId, "The account could not be created, something has failed in the encryption");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Boolean isOnline(Long accountId, String transactionId) {
        return obtainAccountPort.findById(accountId).map(AccountEntity::isOnline).orElse(null);
    }

}
