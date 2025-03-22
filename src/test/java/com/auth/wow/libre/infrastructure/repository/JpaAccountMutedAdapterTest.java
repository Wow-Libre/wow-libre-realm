package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account_muted.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaAccountMutedAdapterTest {
    @Mock
    private AccountMutedRepository accountMutedRepository;

    @InjectMocks
    private JpaAccountMutedAdapter jpaAccountMutedAdapter;

    @Test
    void getAccountMuted_ShouldReturnMostRecentMutedEntity_WhenExists() {
        Long accountId = 1L;
        String transactionId = "tx123";

        AccountMutedEntity olderMute = new AccountMutedEntity();
        olderMute.setMutedate(1710000000L);
        olderMute.setMuteTime(10L);
        olderMute.setMutedBy("admin1");
        olderMute.setMuteReason("Spam");
        olderMute.setGuid(accountId);

        AccountMutedEntity recentMute = new AccountMutedEntity();
        recentMute.setMutedate(1720000000L);
        recentMute.setMuteTime(15L);
        recentMute.setMutedBy("admin2");
        recentMute.setMuteReason("Abusive language");
        recentMute.setGuid(accountId);

        when(accountMutedRepository.findByGuid(accountId)).thenReturn(List.of(olderMute, recentMute));

        AccountMuted result = jpaAccountMutedAdapter.getAccountMuted(accountId, transactionId);

        assertNotNull(result);
        assertEquals("admin2", result.mutedBy());
        assertEquals("Abusive language", result.muteReason());
        verify(accountMutedRepository, times(1)).findByGuid(accountId);
    }

    @Test
    void getAccountMuted_ShouldReturnNull_WhenNoMuteExists() {
        Long accountId = 1L;
        String transactionId = "tx123";
        when(accountMutedRepository.findByGuid(accountId)).thenReturn(List.of());

        AccountMuted result = jpaAccountMutedAdapter.getAccountMuted(accountId, transactionId);

        assertNull(result);
        verify(accountMutedRepository, times(1)).findByGuid(accountId);
    }

    @Test
    void delete_ShouldDeleteAllMutedEntries() {
        Long accountId = 1L;
        String transactionId = "tx123";
        AccountMutedEntity muteEntity = new AccountMutedEntity();
        muteEntity.setGuid(accountId);

        when(accountMutedRepository.findByGuid(accountId)).thenReturn(List.of(muteEntity));

        jpaAccountMutedAdapter.delete(accountId, transactionId);

        verify(accountMutedRepository, times(1)).findByGuid(accountId);
        verify(accountMutedRepository, times(1)).deleteAll(any());
    }
}
