package com.auth.wow.libre.application.services.account_muted;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.in.account_muted.*;
import com.auth.wow.libre.domain.ports.out.account_muted.*;
import org.springframework.stereotype.*;

import java.time.*;

@Service
public class AccountMutedService implements AccountMutedPort {
    private final ObtainAccountMutedPort obtainAccountMutedPort;
    private final DeleteAccountMutedPort deleteAccountMutedPort;

    public AccountMutedService(ObtainAccountMutedPort obtainAccountMutedPort,
                               DeleteAccountMutedPort deleteAccountMutedPort) {
        this.obtainAccountMutedPort = obtainAccountMutedPort;
        this.deleteAccountMutedPort = deleteAccountMutedPort;
    }

    @Override
    public AccountMuted getAccountMuted(Long accountId, String transactionId) {

        AccountMuted accountMuted = obtainAccountMutedPort.getAccountMuted(accountId, transactionId);

        if (accountMuted != null && accountMuted.muteTime().isBefore(LocalDateTime.now())) {
            deleteAccountMutedPort.delete(accountId, transactionId);
            return null;
        }

        return accountMuted;
    }

}
