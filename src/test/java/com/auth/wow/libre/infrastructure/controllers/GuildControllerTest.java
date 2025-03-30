package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.guild.*;
import com.auth.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuildControllerTest {

    @Mock
    private GuildPort guildPort;

    @InjectMocks
    private GuildController guildController;

    private static final String TRANSACTION_ID = "12345";
    private static final Long GUILD_ID = 1L;
    private static final Long ACCOUNT_ID = 10L;
    private static final Long CHARACTER_ID = 20L;

    @Test
    void testGetGuilds_Success() {
        GuildsDto guildsDto = new GuildsDto();
        when(guildPort.findAll(10, 1, "test", TRANSACTION_ID)).thenReturn(guildsDto);

        ResponseEntity<GenericResponse<GuildsDto>> response =
                guildController.guilds(TRANSACTION_ID, 10, 1, "test");

        verify(guildPort, times(1)).findAll(10, 1, "test", TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetGuilds_NoContent() {
        when(guildPort.findAll(10, 1, "test", TRANSACTION_ID)).thenReturn(null);

        ResponseEntity<GenericResponse<GuildsDto>> response =
                guildController.guilds(TRANSACTION_ID, 10, 1, "test");

        verify(guildPort, times(1)).findAll(10, 1, "test", TRANSACTION_ID);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetGuild_Success() {
        GuildDto guildDto = new GuildDto(
                1L,
                "Shadow Warriors",
                "Arthas",
                2L,  // emblemStyle
                16711680L,  // emblemColor (Red in RGB)
                3L,  // borderStyle
                255L,  // borderColor (Blue in RGB)
                "Elite PvP Guild",  // info
                "Raid tonight at 8PM!",  // motd
                new Date(),  // createDate
                5000000L,  // bankMoney
                50L,  // members
                new GuildDto.Cta("DEFEND_CASTLE", false),  // cta
                true,  // publicAccess
                "5,000,000 Gold",  // formattedBankMoney
                true,  // isLeader
                "https://discord.gg/guildchat",  // discord
                false  // multiFaction
        );

        when(guildPort.detail(GUILD_ID, TRANSACTION_ID)).thenReturn(guildDto);

        ResponseEntity<GenericResponse<GuildDto>> response =
                guildController.guild(TRANSACTION_ID, GUILD_ID);

        verify(guildPort, times(1)).detail(GUILD_ID, TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testAttach_Success() {
        ResponseEntity<GenericResponse<Void>> response =
                guildController.attach(TRANSACTION_ID, ACCOUNT_ID, GUILD_ID, CHARACTER_ID);

        verify(guildPort, times(1)).attach(GUILD_ID, ACCOUNT_ID, CHARACTER_ID, TRANSACTION_ID);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUnInviteGuild_Success() {
        ResponseEntity<GenericResponse<Void>> response =
                guildController.unInviteGuild(TRANSACTION_ID, CHARACTER_ID, ACCOUNT_ID);

        verify(guildPort, times(1)).unInviteGuild(ACCOUNT_ID, CHARACTER_ID, TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGuildCharacterId_Success() {
        GuildDto guildDto = new GuildDto(
                1L,
                "Shadow Warriors",
                "Arthas",
                2L,  // emblemStyle
                16711680L,  // emblemColor (Red in RGB)
                3L,  // borderStyle
                255L,  // borderColor (Blue in RGB)
                "Elite PvP Guild",  // info
                "Raid tonight at 8PM!",  // motd
                new Date(),  // createDate
                5000000L,  // bankMoney
                50L,  // members
                new GuildDto.Cta("DEFEND_CASTLE", true),  // cta
                true,  // publicAccess
                "5,000,000 Gold",  // formattedBankMoney
                true,  // isLeader
                "https://discord.gg/guildchat",  // discord
                false  // multiFaction
        );
        when(guildPort.detail(ACCOUNT_ID, CHARACTER_ID, TRANSACTION_ID)).thenReturn(guildDto);

        ResponseEntity<GenericResponse<GuildDto>> response =
                guildController.guildCharacterId(TRANSACTION_ID, CHARACTER_ID, ACCOUNT_ID);

        verify(guildPort, times(1)).detail(ACCOUNT_ID, CHARACTER_ID, TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdate_Success() {
        UpdateGuildDto request = new UpdateGuildDto();
        request.setAccountId(ACCOUNT_ID);
        request.setCharacterId(CHARACTER_ID);
        request.setDiscord("discordLink");
        request.setMultiFaction(true);
        request.setPublic(false);

        ResponseEntity<GenericResponse<Void>> response =
                guildController.update(TRANSACTION_ID, request);

        verify(guildPort, times(1)).update(ACCOUNT_ID, CHARACTER_ID, "discordLink", true, false, TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
