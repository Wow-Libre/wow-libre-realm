package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaAccountAdapterTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private JpaAccountAdapter jpaAccountAdapter;

    private AccountEntity account;

    @BeforeEach
    void setUp() {
        account = new AccountEntity();
        account.setId(1L);
        account.setUsername("testUser");
        account.setEmail("test@example.com");
    }

    @Test
    void save_ShouldReturnSavedAccount() {
        when(accountRepository.save(account)).thenReturn(account);
        AccountEntity savedAccount = jpaAccountAdapter.save(account);
        assertNotNull(savedAccount);
        assertEquals("testUser", savedAccount.getUsername());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void findByUsername_ShouldReturnAccountIfExists() {
        when(accountRepository.findByUsername("testUser")).thenReturn(Optional.of(account));
        Optional<AccountEntity> result = jpaAccountAdapter.findByUsername("testUser");
        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    void findByUserId_ShouldReturnAccountsList() {
        when(accountRepository.findByUserId(1L)).thenReturn(List.of(account));
        List<AccountEntity> result = jpaAccountAdapter.findByUserId(1L);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findById_ShouldReturnAccountIfExists() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Optional<AccountEntity> result = jpaAccountAdapter.findById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void findByAll_ShouldReturnPagedResults() {
        Page<AccountEntity> page = new PageImpl<>(List.of(account));
        when(accountRepository.findAllByEmailContainingIgnoreCase(eq("test"), any(PageRequest.class))).thenReturn(page);
        List<AccountEntity> result = jpaAccountAdapter.findByAll(10, 0, "test");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void count_ShouldReturnTotalCount() {
        when(accountRepository.count()).thenReturn(5L);
        Long count = jpaAccountAdapter.count();
        assertEquals(5L, count);
    }

    @Test
    void countOnline_ShouldReturnOnlineCount() {
        when(accountRepository.countByOnlineTrue()).thenReturn(2L);
        Long count = jpaAccountAdapter.countOnline("txn123");
        assertEquals(2L, count);
    }

    @Test
    void findByIdAndUserId_ShouldReturnAccount() {
        when(accountRepository.findByIdAndUserId(1L, 12L)).thenReturn(Optional.of(account));
        Optional<AccountEntity> count = jpaAccountAdapter.findByIdAndUserId(1L, 12L);
        assertNotNull(count);
    }

    @Test
    void metrics_ShouldReturnMetricsProjection() {
        MetricsProjection mockMetrics = mock(MetricsProjection.class);
        when(accountRepository.fetchMetrics()).thenReturn(mockMetrics);
        MetricsProjection result = jpaAccountAdapter.metrics("txn123");
        assertNotNull(result);
        assertEquals(mockMetrics, result);
        verify(accountRepository, times(1)).fetchMetrics();
    }
}
