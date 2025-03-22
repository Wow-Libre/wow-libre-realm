package com.auth.wow.libre.infrastructure.repositories.auth.account_banned;

import com.auth.wow.libre.domain.ports.out.account_banned.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class JpaAccountBannedAdapter implements ObtainAccountBanned, SaveAccountBanned {

    private final AccountBannedRepository accountBannedRepository;

    public JpaAccountBannedAdapter(AccountBannedRepository accountBannedRepository) {
        this.accountBannedRepository = accountBannedRepository;
    }

    @Override
    public Optional<AccountBannedEntity> getAccountBanned(Long accountId) {
        return accountBannedRepository.findByAccountIdAndActiveIsTrue(accountId);
    }

    @Override
    public void save(Long accountId, Long banDate, Long unBanDate, String bannedBy, String banReason, boolean active) {
        AccountBannedEntity accountBanned = new AccountBannedEntity();
        accountBanned.setActive(active);
        accountBanned.setBandate(banDate);
        accountBanned.setUnbandate(unBanDate);
        accountBanned.setBannedby(bannedBy);
        accountBanned.setBanreason(banReason);
        accountBanned.setAccountId(accountId);
        accountBannedRepository.save(accountBanned);
    }

}
