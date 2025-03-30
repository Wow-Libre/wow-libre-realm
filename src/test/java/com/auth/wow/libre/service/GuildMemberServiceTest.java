package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.guild_member.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.out.guild_members.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuildMemberServiceTest {
    @Mock
    private ObtainGuildMember obtainGuildMember;

    @Mock
    private SaveGuildMember saveGuildMember;

    @InjectMocks
    private GuildMemberService guildMemberService;

    private final Long guildId = 1L;
    private final Long characterId = 100L;
    private final String transactionId = "tx-123";

    @BeforeEach
    void setUp() {
        guildMemberService = new GuildMemberService(obtainGuildMember, saveGuildMember);
    }

    @Test
    void testAccountMemberGuildId() {
        when(obtainGuildMember.numberMembers(guildId)).thenReturn(5L);

        long result = guildMemberService.accountMemberGuildId(guildId);

        assertEquals(5L, result);
    }

    @Test
    void testSaveGuildMember() {
        GuildMemberModel guildMemberModel = mock(GuildMemberModel.class);

        when(guildMemberModel.guildId()).thenReturn(guildId);
        when(guildMemberModel.characterId()).thenReturn(characterId);
        when(guildMemberModel.rank()).thenReturn(1);

        guildMemberService.saveGuildMember(guildMemberModel, transactionId);

        verify(saveGuildMember, times(1)).save(any(GuildMemberEntity.class));
    }

    @Test
    void testGuildMemberByCharacterId_Success() {
        GuildMemberEntity guildMemberEntity = new GuildMemberEntity();
        guildMemberEntity.setId(guildId);
        guildMemberEntity.setGuid(characterId);
        guildMemberEntity.setRank(1);

        when(obtainGuildMember.guildMemberByCharacterId(characterId, transactionId))
                .thenReturn(Optional.of(guildMemberEntity));

        GuildMemberModel result = guildMemberService.guildMemberByCharacterId(characterId, transactionId);

        assertNotNull(result);
        assertEquals(guildId, result.guildId());
        assertEquals(characterId, result.characterId());
        assertEquals(1, result.rank());
    }

    @Test
    void testGuildMemberByCharacterId_NotFound() {
        when(obtainGuildMember.guildMemberByCharacterId(characterId, transactionId))
                .thenReturn(Optional.empty());

        GuildMemberModel result = guildMemberService.guildMemberByCharacterId(characterId, transactionId);

        assertNull(result);
    }
}
