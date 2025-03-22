package com.auth.wow.libre.application.services.account_banned;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.in.account_banned.*;
import com.auth.wow.libre.domain.ports.out.account_banned.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.stereotype.*;

@Service
public class AccountBannedService implements AccountBannedPort, SaveBannedPort {
    private final ObtainAccountBanned obtainAccountBanned;
    private final SaveAccountBanned saveAccountBanned;

    public AccountBannedService(ObtainAccountBanned obtainAccountBanned, SaveAccountBanned saveAccountBanned) {
        this.obtainAccountBanned = obtainAccountBanned;
        this.saveAccountBanned = saveAccountBanned;
    }

    @Override
    public AccountBanned getAccountBanned(Long accountId) {
        return obtainAccountBanned.getAccountBanned(accountId).map(this::mapToModel).orElse(null);
    }

    private AccountBanned mapToModel(AccountBannedEntity accountBannedEntity) {
        return new AccountBanned(
                accountBannedEntity.getAccountId(),
                new java.util.Date(accountBannedEntity.getBandate() * 1000),
                new java.util.Date(accountBannedEntity.getUnbandate() * 1000),
                accountBannedEntity.getBannedby(),
                accountBannedEntity.getBanreason(),
                accountBannedEntity.isActive()
        );
    }

    @Override
    public void save(Long accountId, Long banDate, Long unBanDate, String bannedBy, String banReason, boolean active) {
        saveAccountBanned.save(accountId, banDate, unBanDate, bannedBy, banReason, active);
    }
}
