package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.guild.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaGuildAdapterTest {
    @Mock
    private GuildRepository guildRepository;

    @InjectMocks
    private JpaGuildAdapter jpaGuildAdapter;

    private GuildEntity guildEntity;
    private static final Long GUILD_ID = 1L;
    private static final String TRANSACTION_ID = "txn-123";
    private static final String SEARCH_TERM = "GuildName";

    @BeforeEach
    void setUp() {
        guildEntity = new GuildEntity();
    }

    @Test
    void testGetGuilds_WithSearch() {
        when(guildRepository.findByNameContainingIgnoreCase(SEARCH_TERM)).thenReturn(List.of(guildEntity));
        List<GuildEntity> result = jpaGuildAdapter.getGuilds(10, 0, SEARCH_TERM, TRANSACTION_ID);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(guildRepository, times(1)).findByNameContainingIgnoreCase(SEARCH_TERM);
    }

    @Test
    void testGetGuilds_WithoutSearch() {
        Page<GuildEntity> page = new PageImpl<>(List.of(guildEntity));
        when(guildRepository.findAll(PageRequest.of(0, 10))).thenReturn(page.toList());
        List<GuildEntity> result = jpaGuildAdapter.getGuilds(10, 0, "", TRANSACTION_ID);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(guildRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    void testGetGuild() {
        when(guildRepository.findById(GUILD_ID)).thenReturn(Optional.of(guildEntity));

        Optional<GuildEntity> result = jpaGuildAdapter.getGuild(GUILD_ID);

        assertTrue(result.isPresent());
        assertEquals(guildEntity, result.get());
        verify(guildRepository, times(1)).findById(GUILD_ID);
    }

    @Test
    void testGetGuildCount() {
        when(guildRepository.countAllGuilds()).thenReturn(5L);

        Long count = jpaGuildAdapter.getGuildCount(TRANSACTION_ID);

        assertEquals(5L, count);
        verify(guildRepository, times(1)).countAllGuilds();
    }

    @Test
    void testCount() {
        when(guildRepository.count()).thenReturn(10L);

        Long count = jpaGuildAdapter.count(TRANSACTION_ID);

        assertEquals(10L, count);
        verify(guildRepository, times(1)).count();
    }

    @Test
    void testSave() {
        jpaGuildAdapter.save(guildEntity, TRANSACTION_ID);
        verify(guildRepository, times(1)).save(guildEntity);
    }
}
