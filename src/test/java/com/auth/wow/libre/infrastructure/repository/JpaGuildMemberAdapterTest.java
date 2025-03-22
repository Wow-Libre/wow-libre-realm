package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.guild_member.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaGuildMemberAdapterTest {
    @Mock
    private GuildMemberRepository guildMemberRepository;

    @InjectMocks
    private JpaGuildMemberAdapter jpaGuildMemberAdapter;

    private GuildMemberEntity guildMemberEntity;
    private static final Long GUILD_ID = 1L;
    private static final Long CHARACTER_ID = 100L;
    private static final String TRANSACTION_ID = "txn-123";

    @BeforeEach
    void setUp() {
        guildMemberEntity = new GuildMemberEntity();
    }

    @Test
    void testNumberMembers() {
        when(guildMemberRepository.countById(GUILD_ID)).thenReturn(10L);

        long count = jpaGuildMemberAdapter.numberMembers(GUILD_ID);

        assertEquals(10L, count);
        verify(guildMemberRepository, times(1)).countById(GUILD_ID);
    }

    @Test
    void testGuildMemberByCharacterId() {
        when(guildMemberRepository.findByGuid(CHARACTER_ID)).thenReturn(Optional.of(guildMemberEntity));

        Optional<GuildMemberEntity> result = jpaGuildMemberAdapter.guildMemberByCharacterId(CHARACTER_ID,
                TRANSACTION_ID);

        assertTrue(result.isPresent());
        assertEquals(guildMemberEntity, result.get());
        verify(guildMemberRepository, times(1)).findByGuid(CHARACTER_ID);
    }

    @Test
    void testSave() {
        jpaGuildMemberAdapter.save(guildMemberEntity);
        verify(guildMemberRepository, times(1)).save(guildMemberEntity);
    }
}
