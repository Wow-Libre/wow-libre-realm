package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.character_skills.*;
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
class CharactersSkillControllerTest {
    @Mock
    private CharacterSkillsPort characterSkillsPort;

    @InjectMocks
    private CharactersSkillController charactersSkillController;

    private AnnouncementDto announcementDto;

    @BeforeEach
    void setUp() {
        announcementDto = new AnnouncementDto();
        announcementDto.setAccountId(1L);
        announcementDto.setUserId(2L);
        announcementDto.setCharacterId(3L);
        announcementDto.setSkillId(4L);
    }

    @Test
    void testProfessions_Success() {
        String transactionId = "12345";
        Long characterId = 1L;
        Long accountId = 2L;
        List<CharacterProfessionsModel> professions = Collections.singletonList(new CharacterProfessionsModel(1,
                "logo.png", "Herrería", 150L, 300L));

        when(characterSkillsPort.getProfessions(characterId, accountId, transactionId)).thenReturn(professions);

        ResponseEntity<GenericResponse<List<CharacterProfessionsModel>>> response =
                charactersSkillController.professions(transactionId, characterId, accountId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professions, Objects.requireNonNull(response.getBody()).getData());
        verify(characterSkillsPort, times(1)).getProfessions(characterId, accountId, transactionId);
    }

    @Test
    void testProfessions_NoContent() {
        String transactionId = "12345";
        Long characterId = 1L;
        Long accountId = 2L;

        when(characterSkillsPort.getProfessions(characterId, accountId, transactionId)).thenReturn(null);

        ResponseEntity<GenericResponse<List<CharacterProfessionsModel>>> response =
                charactersSkillController.professions(transactionId, characterId, accountId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(characterSkillsPort, times(1)).getProfessions(characterId, accountId, transactionId);
    }

    @Test
    void testAnnouncement_Success() {
        String transactionId = "12345";
        String emulator = "AzerothCore";

        ResponseEntity<GenericResponse<Void>> response =
                charactersSkillController.announcement(transactionId, emulator, announcementDto);

        verify(characterSkillsPort, times(1)).professionAnnouncement(
                announcementDto.getUserId(),
                announcementDto.getCharacterId(),
                announcementDto.getAccountId(),
                announcementDto.getSkillId(),
                announcementDto.getMessage(),
                emulator,
                transactionId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
