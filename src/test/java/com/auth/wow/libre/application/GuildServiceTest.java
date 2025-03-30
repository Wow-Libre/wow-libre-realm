package com.auth.wow.libre.application;

import com.auth.wow.libre.application.services.guild.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.guild_member.*;
import com.auth.wow.libre.domain.ports.out.guild.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuildServiceTest {

    @Mock
    private ObtainGuild obtainGuild;
    @Mock
    private GuildMemberPort guildMemberPort;
    @InjectMocks
    private GuildService guildService;


    @Test
    void detail_ShouldThrowNotFoundException_WhenGuildDoesNotExist() {
        when(obtainGuild.getGuild(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> guildService.detail(1L, "txn123"));
    }

    @Test
    void attach_ShouldThrowNotFoundException_WhenGuildDoesNotExist() {
        when(obtainGuild.getGuild(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> guildService.attach(1L, 1L, 1L, "txn123"));
    }


    @Test
    void update_ShouldThrowNotFoundException_WhenUserIsNotGuildMaster() {
        GuildMemberModel guildMember = new GuildMemberModel(1L, 1L, 12);
        when(guildMemberPort.guildMemberByCharacterId(1L, "txn123")).thenReturn(guildMember);

        assertThrows(NotFoundException.class, () -> guildService.update(1L, 1L, "discord", true, true, "txn123"));
    }


    @Test
    void count_ShouldReturnGuildCount() {
        when(obtainGuild.count("txn123")).thenReturn(5L);

        Long result = guildService.count("txn123");

        assertEquals(5L, result);
        verify(obtainGuild, times(1)).count("txn123");
    }


}
