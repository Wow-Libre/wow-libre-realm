package com.auth.wow.libre.infrastructure.repositories.auth.account_muted;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.constant.*;
import com.auth.wow.libre.domain.ports.out.account_muted.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Component
public class JpaAccountMutedAdapter implements ObtainAccountMutedPort, DeleteAccountMutedPort {
    private final AccountMutedRepository accountMutedRepository;

    public JpaAccountMutedAdapter(AccountMutedRepository accountMutedRepository) {
        this.accountMutedRepository = accountMutedRepository;
    }

    @Override
    public AccountMuted getAccountMuted(Long accountId, String transactionId) {
        return accountMutedRepository.findByGuid(accountId).stream().max(Comparator.comparingLong(AccountMutedEntity::getMutedate))
                .map(this::mapToModel).orElse(null);
    }

    private AccountMuted mapToModel(AccountMutedEntity accountMutedEntity) {
        Date dateMuted = Date.from(Instant.ofEpochMilli(accountMutedEntity.getMutedate() * 1000));
        Date desmuteDate = DateHelper.incrementMinutes(dateMuted, accountMutedEntity.getMuteTime().intValue());
        return new AccountMuted(accountMutedEntity.getGuid(),
                DateHelper.getLocalDateTime(dateMuted, "WLCO"),
                DateHelper.getLocalDateTime(desmuteDate, "WLCO"), accountMutedEntity.getMutedBy(),
                accountMutedEntity.getMuteReason());
    }


    @Override
    public void delete(Long accountId, String transactionId) {
        accountMutedRepository.deleteAll(accountMutedRepository.findByGuid(accountId));
    }
}
