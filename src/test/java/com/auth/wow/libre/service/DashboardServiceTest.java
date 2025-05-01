package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.dashboard.*;
import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.dto.view.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.guild.*;
import com.auth.wow.libre.domain.ports.in.server_publications.*;
import com.auth.wow.libre.domain.ports.out.file.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {
    @Mock
    private AccountPort accountPort;

    @Mock
    private CharactersPort charactersPort;

    @Mock
    private GuildPort guildPort;

    @Mock
    private ObtainConfigsServer obtainConfigsServer;

    @Mock
    private SaveConfigsServer saveConfigsServer;

    @InjectMocks
    private DashboardService dashboardService;
    @Mock
    private ServerPublicationsPort serverPublicationsPort;

    private final String transactionId = "tx-123";

    @BeforeEach
    void setUp() {
        dashboardService = new DashboardService(accountPort, charactersPort, guildPort, obtainConfigsServer,
                saveConfigsServer, serverPublicationsPort);

    }

    @Test
    void testMetricsCount() {
        MetricsProjection metricsProjection = mock(MetricsProjection.class);
        FactionsDto factionsDto = mock(FactionsDto.class);
        when(accountPort.metrics(transactionId)).thenReturn(metricsProjection);
        when(charactersPort.factions(transactionId)).thenReturn(factionsDto);
        when(guildPort.count(transactionId)).thenReturn(10L);
        LevelRangeDTO levelRangeDTO = new LevelRangeDTO();
        levelRangeDTO.setLevelRange("");
        levelRangeDTO.setLevelRange("20-30");

        when(charactersPort.findUserCountsByLevelRange(transactionId)).thenReturn(List.of(levelRangeDTO));

        when(metricsProjection.getTotalUsers()).thenReturn(100L);
        when(metricsProjection.getOnlineUsers()).thenReturn(50L);
        when(metricsProjection.getTotalUsersExternal()).thenReturn(20L);
        when(factionsDto.getCharacters()).thenReturn(200L);
        when(factionsDto.getHorda()).thenReturn(120L);
        when(factionsDto.getAlianza()).thenReturn(80L);

        DashboardMetricsDto result = dashboardService.metricsCount(transactionId);

        assertNotNull(result);
        assertEquals(100, result.getTotalUsers());
        assertEquals(50, result.getOnlineUsers());
        assertEquals(20, result.getExternalRegistrations());
        assertEquals(200, result.getCharacterCount());
        assertEquals(10, result.getTotalGuilds());
        assertEquals(120, result.getHordas());
        assertEquals(80, result.getAlianzas());
    }

    @Test
    void testUpdateMailAccount() {
        dashboardService.updateMailAccount("user1", "new@mail.com", transactionId);
        verify(accountPort, times(1)).updateMail("user1", "new@mail.com", transactionId);
    }

    @Test
    void testBannedUser() {
        AccountBanDto banDto = new AccountBanDto();
        banDto.setBannedBy("admin");
        banDto.setBanReason("reason");
        banDto.setDays(1);
        banDto.setHours(2);
        banDto.setMinutes(3);
        banDto.setSeconds(4);
        banDto.setUsername("user1");
        dashboardService.bannedUser(banDto, transactionId);
        verify(accountPort, times(1)).bannedUser("user1", 1, 2, 3, 4, "admin", "reason", transactionId);
    }

    @Test
    void testGetFileConfig_Success() {
        AuthServerConfig config = mock(AuthServerConfig.class);
        when(obtainConfigsServer.getFileConfigServer("path/to/config", transactionId)).thenReturn(config);
        when(config.valores()).thenReturn(Map.of("key1", "value1", "key2", "value2"));

        Map<String, String> result = dashboardService.getFileConfig("path/to/config", transactionId);

        assertNotNull(result);
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    void testGetFileConfig_NotFound() {
        when(obtainConfigsServer.getFileConfigServer("path/to/config", transactionId)).thenReturn(null);

        InternalException exception = assertThrows(InternalException.class,
                () -> dashboardService.getFileConfig("path/to/config", transactionId));

        assertEquals("It was not possible to find and extract the file", exception.getMessage());
    }

    @Test
    void testUpdateFileConfig() {
        dashboardService.updateFileConfig("path/to/config", Map.of("key1", "newValue"), transactionId);
        verify(saveConfigsServer, times(1)).updateConfigFile("path/to/config", Map.of("key1", "newValue"));
    }

    @Test
    void testPublicationsReturnsMappedCards() {
        // Arrange
        Card entity1 = new Card(1L,"img1.png", "Título 1", "Descripción 1");
        Card entity2 = new Card(2L,"img2.png", "Título 2", "Descripción 2");

        when(serverPublicationsPort.publications()).thenReturn(List.of(entity1, entity2));

        // Act
        List<Card> cards = dashboardService.findByPublications(transactionId);

        // Assert
        assertNotNull(cards);
        assertEquals(2, cards.size());

        Card card1 = cards.get(0);
        assertEquals(1L, card1.getId());
        assertEquals("img1.png", card1.getIcon());
        assertEquals("Título 1", card1.getTitle());
        assertEquals("Descripción 1", card1.getDescription());

        Card card2 = cards.get(1);
        assertEquals(2L, card2.getId());
        assertEquals("img2.png", card2.getIcon());
        assertEquals("Título 2", card2.getTitle());
        assertEquals("Descripción 2", card2.getDescription());

        verify(serverPublicationsPort, times(1)).publications();
    }
}
