package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account_banned.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaAccountBannedAdapterTest {
    @Mock
    private AccountBannedRepository accountBannedRepository;

    @InjectMocks
    private JpaAccountBannedAdapter jpaAccountBannedAdapter;

   
    @Test
    void getAccountBanned_ShouldReturnAccountBannedEntity_WhenExists() {
        long accountId = 1L;
        AccountBannedEntity bannedEntity = new AccountBannedEntity();
        bannedEntity.setActive(true);

        when(accountBannedRepository.findByAccountIdAndActiveIsTrue(accountId)).thenReturn(Optional.of(bannedEntity));

        Optional<AccountBannedEntity> result = jpaAccountBannedAdapter.getAccountBanned(accountId);

        assertTrue(result.isPresent());
        assertTrue(result.get().isActive());
        verify(accountBannedRepository, times(1)).findByAccountIdAndActiveIsTrue(accountId);
    }

    @Test
    void getAccountBanned_ShouldReturnEmptyOptional_WhenNotExists() {
        long accountId = 1L;
        when(accountBannedRepository.findByAccountIdAndActiveIsTrue(accountId)).thenReturn(Optional.empty());

        Optional<AccountBannedEntity> result = jpaAccountBannedAdapter.getAccountBanned(accountId);

        assertFalse(result.isPresent());
        verify(accountBannedRepository, times(1)).findByAccountIdAndActiveIsTrue(accountId);
    }

    @Test
    void save_ShouldSaveAccountBannedEntity() {
        Long banDate = 1710451200000L;
        Long unBanDate = 1713053200000L;
        String bannedBy = "admin";
        String banReason = "Violation of rules";
        boolean active = true;
        Long accountId = 1L;
        AccountBannedEntity accountBanned = new AccountBannedEntity();
        accountBanned.setActive(active);

        when(accountBannedRepository.save(any(AccountBannedEntity.class))).thenReturn(accountBanned);

        jpaAccountBannedAdapter.save(accountId, banDate, unBanDate, bannedBy, banReason, active);

        verify(accountBannedRepository, times(1)).save(any(AccountBannedEntity.class));
    }
}
