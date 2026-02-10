package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.character_skill.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterSkillServiceTest {
    @Mock
    private CharactersPort charactersPort;
    @InjectMocks
    private CharacterSkillService characterSkillService;

    private static final Long CHARACTER_ID = 1L;
    private static final Long ACCOUNT_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long SKILL_ID = 101L;
    private static final String TRANSACTION_ID = "txn123";

 
    @Test
    void getProfessions_ShouldThrowException_WhenCharacterNotFound() {
        when(charactersPort.getCharacter(CHARACTER_ID, ACCOUNT_ID, TRANSACTION_ID)).thenReturn(null);

        Exception exception = assertThrows(InternalException.class, () ->
                characterSkillService.getProfessions(CHARACTER_ID, ACCOUNT_ID, TRANSACTION_ID)
        );

        assertEquals("The character with the associated account could not be found.", exception.getMessage());
    }


    @Test
    void professionAnnouncement_ShouldThrowException_WhenCharacterNotFound() {
        when(charactersPort.getCharacter(CHARACTER_ID, ACCOUNT_ID, TRANSACTION_ID)).thenReturn(null);

        Exception exception = assertThrows(InternalException.class, () ->
                characterSkillService.professionAnnouncement(USER_ID, CHARACTER_ID, ACCOUNT_ID, SKILL_ID, "Test Message",
                        "AzerothCore", TRANSACTION_ID)
        );

        assertEquals("The character with the associated account could not be found.", exception.getMessage());
    }

}
