package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.account_banned.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.out.account_banned.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountBannedServiceTest {
    @Mock
    private ObtainAccountBanned obtainAccountBanned;

    @Mock
    private SaveAccountBanned saveAccountBanned;

    @InjectMocks
    private AccountBannedService accountBannedService;

    private static final Long ACCOUNT_ID = 100L;
    private static final Long BAN_DATE = 1710000000L;
    private static final Long UN_BAN_DATE = 1720000000L;
    private static final String BANNED_BY = "Admin";
    private static final String BAN_REASON = "Violation of rules";
    private static final boolean ACTIVE = true;

    @BeforeEach
    void setUp() {
        accountBannedService = new AccountBannedService(obtainAccountBanned, saveAccountBanned);
    }

    @Test
    void testGetAccountBanned_Success() {
        AccountBannedEntity bannedEntity = mock(AccountBannedEntity.class);
        when(bannedEntity.getAccountId()).thenReturn(ACCOUNT_ID);
        when(bannedEntity.getBandate()).thenReturn(BAN_DATE);
        when(bannedEntity.getUnbandate()).thenReturn(UN_BAN_DATE);
        when(bannedEntity.getBannedby()).thenReturn(BANNED_BY);
        when(bannedEntity.getBanreason()).thenReturn(BAN_REASON);
        when(bannedEntity.isActive()).thenReturn(ACTIVE);
        when(obtainAccountBanned.getAccountBanned(ACCOUNT_ID)).thenReturn(Optional.of(bannedEntity));

        AccountBanned result = accountBannedService.getAccountBanned(ACCOUNT_ID);

        assertNotNull(result);
        assertEquals(ACCOUNT_ID, result.id);
        assertEquals(new Date(BAN_DATE * 1000), result.bandate);
        assertEquals(new Date(UN_BAN_DATE * 1000), result.unbandate);
        assertEquals(BANNED_BY, result.bannedBy);
        assertEquals(BAN_REASON, result.banReason);
        assertTrue(result.active);
    }

    @Test
    void testGetAccountBanned_NotFound() {
        when(obtainAccountBanned.getAccountBanned(ACCOUNT_ID)).thenReturn(Optional.empty());

        AccountBanned result = accountBannedService.getAccountBanned(ACCOUNT_ID);

        assertNull(result);
    }

    @Test
    void testSaveAccountBanned() {
        accountBannedService.save(ACCOUNT_ID, BAN_DATE, UN_BAN_DATE, BANNED_BY, BAN_REASON, ACTIVE);
        verify(saveAccountBanned, times(1)).save(ACCOUNT_ID, BAN_DATE, UN_BAN_DATE, BANNED_BY, BAN_REASON, ACTIVE);
    }
}
