package com.auth.wow.libre.application;

import com.auth.wow.libre.application.services.guild.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.in.guild_member.*;
import com.auth.wow.libre.domain.ports.out.guild.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import jakarta.xml.bind.*;
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
    @Mock
    private SaveGuild saveGuild;
    @Mock
    private CharactersPort charactersPort;
    @Mock
    private ExecuteCommandsPort executeCommandsPort;

    private GuildService guildService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        guildService = new GuildService(obtainGuild, guildMemberPort, saveGuild, charactersPort, executeCommandsPort);
    }

    @Test
    void testFindAll() {
        List<GuildEntity> mockGuilds = getGuildEntities();
        when(obtainGuild.getGuilds(10, 0, null, "txn123")).thenReturn(mockGuilds);
        when(obtainGuild.getGuildCount("txn123")).thenReturn(1L);
        when(charactersPort.getCharacter(any(), any())).thenReturn(getCharacterDetailDto());

        // Llamada al método
        GuildsDto result = guildService.findAll(10, 0, null, "txn123");

        // Verificaciones
        assertNotNull(result);
        assertEquals(1, result.getGuilds().size());
        assertEquals(1L, result.getSize());
    }

    private static List<GuildEntity> getGuildEntities() {
        GuildEntity guild = getGuildEntity(1, true);
        return List.of(guild);
    }

    @Test
    void testDetailWhenGuildExists() {
        GuildEntity guild = getGuildEntity(2, true);


        when(obtainGuild.getGuild(any())).thenReturn(Optional.of(guild));
        when(charactersPort.getCharacter(any(), any())).thenReturn(getCharacterDetailDto());

        // Llamada al método
        GuildDto result = guildService.detail(1L, "txn123");

        // Verificaciones
        assertNotNull(result);
        assertEquals("Guild1", result.name);
        assertEquals("CharacterName", result.leaderName);
    }


    @Test
    void testDetailWhenGuildDoesNotExist() {
        // Mock de dependencias
        when(obtainGuild.getGuild(999L)).thenReturn(Optional.empty());

        // Llamada al método y verificación de la excepción
        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                guildService.detail(999L, "txn123")
        );

        assertEquals("The requested guild does not exist", thrown.getMessage());
    }

    @Test
    void testAttachWhenGuildIsPublic() throws JAXBException {
        GuildEntity guild = getGuildEntity(1, true);

        when(obtainGuild.getGuild(1L)).thenReturn(Optional.of(guild));
        when(charactersPort.getCharacter(1L, 1L, "txn123"))
                .thenReturn(getCharacterDetailDto());
        when(charactersPort.getCharacter(any(), any()))
                .thenReturn(getCharacterDetailDto());
        // Llamada al método
        guildService.attach(1L, 1L, 1L, "AzerothCore", "txn123");

        // Verificaciones
        verify(executeCommandsPort, times(1)).execute(any(), any(), any());
    }

    private static GuildEntity getGuildEntity(int leaderGuid, boolean publicAccess) {
        GuildEntity guild = new GuildEntity();

        guild.setId(1L);
        guild.setName("Guild1");
        guild.setLeaderGuid(leaderGuid);
        guild.setEmblemStyle(1L);
        guild.setEmblemColor(1L);
        guild.setBorderStyle(1L);
        guild.setBorderColor(1L);
        guild.setInfo("info");
        guild.setMotd("motd");
        guild.setCreateDate(1620000000L);
        guild.setBankMoney(5000L);
        guild.setPublicAccess(publicAccess);
        guild.setDiscord("discord");
        guild.setMultiFaction(false);
        return guild;
    }

    @Test
    void testAttachWhenCharactersDoesNotExist() {
        GuildEntity guild = getGuildEntity(1, true);

        when(obtainGuild.getGuild(1L)).thenReturn(Optional.of(guild));
        when(charactersPort.getCharacter(1L, 1L, "txn123"))
                .thenReturn(null);
        when(charactersPort.getCharacter(any(), any()))
                .thenReturn(getCharacterDetailDto());
        // Llamada al método
        InternalException thrown = assertThrows(InternalException.class, () ->
                guildService.attach(1L, 1L, 1L, "AzerothCore", "txn123")
        );

        // Verificaciones
        assertEquals("The requested characters does not exist", thrown.getMessage());
    }

    @Test
    void testAttachWhenGetGuildDoesNotExist() {

        when(obtainGuild.getGuild(1L)).thenReturn(Optional.empty());

        // Llamada al método
        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                guildService.attach(1L, 1L, 1L, "AzerothCore", "txn123")
        );

        // Verificaciones
        assertEquals("The requested guild does not exist", thrown.getMessage());
    }

    @Test
    void testAttachWhenGuildIsNotPublic() {
        GuildEntity guild = getGuildEntity(1, false);
        when(obtainGuild.getGuild(1L)).thenReturn(Optional.of(guild));
        when(charactersPort.getCharacter(1L, ""))
                .thenReturn(getCharacterDetailDto());

        InternalException thrown = assertThrows(InternalException.class, () ->
                guildService.attach(1L, 1L, 1L, "AzerothCore", "txn123")
        );

        assertEquals("The brotherhood is currently not public", thrown.getMessage());
    }

    @Test
    void testUnInviteGuildWhenCharacterExists() throws JAXBException {
        // Mock de dependencias
        CharacterDetailDto mockCharacter = new CharacterDetailDto(CharacterModel.builder().guid(1L).build());
        when(charactersPort.getCharacter(1L, 1L, "txn123")).thenReturn(mockCharacter);

        GuildMemberModel mockGuildMember = new GuildMemberModel(1L, 1L, 1);
        when(guildMemberPort.guildMemberByCharacterId(1L, "txn123")).thenReturn(mockGuildMember);

        guildService.unInviteGuild(1L, 1L, "AzerothCore", "txn123");

        // Verificaciones
        verify(executeCommandsPort, times(1)).execute(any(), any(), any());
    }

    @Test
    void testDetailWhenGuildNotFound() {
        GuildMemberModel guildMember = new GuildMemberModel(1L, 1L, 1);
        when(guildMemberPort.guildMemberByCharacterId(any(), any())).thenReturn(guildMember);
        when(obtainGuild.getGuild(any())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                guildService.detail(1L, 1L, "txn123")
        );

        assertEquals("The requested guild does not exist", thrown.getMessage());
    }

    @Test
    void testDetailWhenGuildMemberNotFound() {
        // Simulamos que el miembro existe, pero no se encuentra el gremio
        when(guildMemberPort.guildMemberByCharacterId(any(), any())).thenReturn(null);

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                guildService.detail(1L, 1L, "txn123")
        );

        assertEquals("The requested guild does not exist", thrown.getMessage());
    }


    @Test
    void testUnInviteGuildWhenCharacterExistsByRankInvalid() {
        CharacterDetailDto mockCharacter = new CharacterDetailDto(CharacterModel.builder().guid(1L).build());
        when(charactersPort.getCharacter(1L, 1L, "txn123")).thenReturn(mockCharacter);

        GuildMemberModel mockGuildMember = new GuildMemberModel(1L, 1L, 0);
        when(guildMemberPort.guildMemberByCharacterId(1L, "txn123")).thenReturn(mockGuildMember);

        InternalException thrown = assertThrows(InternalException.class, () -> guildService.unInviteGuild(1L, 1L,
                "AzerothCore", "txn123"));
        assertEquals("You cannot leave the guild without leaving a guild master.", thrown.getMessage());
    }

    @Test
    void testUnInviteGuildWhenCharacterNotExists() {
        // Mock de dependencias
        CharacterDetailDto mockCharacter = new CharacterDetailDto(CharacterModel.builder().guid(1L).build());
        when(charactersPort.getCharacter(1L, 1L, "txn123")).thenReturn(mockCharacter);
        when(guildMemberPort.guildMemberByCharacterId(1L, "txn123")).thenReturn(null);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> guildService.unInviteGuild(1L, 1L,
                "AzerothCore", "txn123"));
        assertEquals("The requested guild does not exist", thrown.getMessage());

    }

    @Test
    void testUnInviteGuildWhenCharacterDoesNotExist() {
        // Mock de dependencias
        when(charactersPort.getCharacter(1L, 1L, "txn123")).thenReturn(null);

        // Llamada al método y verificación de la excepción
        InternalException thrown = assertThrows(InternalException.class, () ->
                guildService.unInviteGuild(1L, 1L, "AzerothCore", "txn123"));

        assertEquals("The requested characters does not exist", thrown.getMessage());
    }

    @Test
    void testUpdateGuildInfoWhenGuildMaster() {
        // Mock de dependencias
        GuildMemberModel mockGuildMember = new GuildMemberModel(1L, 1L, 0);
        when(guildMemberPort.guildMemberByCharacterId(1L, "txn123")).thenReturn(mockGuildMember);
        GuildEntity mockGuild = new GuildEntity();
        mockGuild.setId(1L);
        when(obtainGuild.getGuild(1L)).thenReturn(Optional.of(mockGuild));

        // Llamada al método
        guildService.update(1L, 1L, "newDiscord", true, false, "txn123");

        // Verificaciones
        verify(saveGuild).save(mockGuild, "txn123");
    }

    private static CharacterDetailDto getCharacterDetailDto() {
        return new CharacterDetailDto(CharacterModel.builder()
                .guid(123L)
                .account(456L)
                .name("CharacterName")
                .raceLogo("raceLogoUrl")
                .raceName("Human")
                .raceId(1)
                .classCharacters(2)
                .className("Warrior")
                .classLogo("warriorLogoUrl")
                .gender(1)  // 1 podría ser masculino
                .level(60)
                .xp(1500000)
                .money(1000.50)
                .facialStyle(1)
                .bankSlots(6)
                .positionX(100.0)
                .positionY(200.0)
                .positionZ(300.0)
                .map(1)
                .online(1)
                .totalTime(360000)
                .logoutTime(1620000000)
                .yesterdayKills(15)
                .gold(1000L)
                .silver(500L)
                .copper(100L)
                .build());
    }

}
