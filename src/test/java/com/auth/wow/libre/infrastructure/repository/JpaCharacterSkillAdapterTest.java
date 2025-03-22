package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.character_skills.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaCharacterSkillAdapterTest {

    @Mock
    private CharacterSkillsRepository characterSkillsRepository;

    @InjectMocks
    private JpaCharacterSkillAdapter jpaCharacterSkillAdapter;

    private CharacterSkillsEntity characterSkillsEntity;
    private static final Long CHARACTER_ID = 1L;

    @BeforeEach
    void setUp() {
        characterSkillsEntity = new CharacterSkillsEntity();
    }

    @Test
    void testGetCharacterIdSkills() {
        List<CharacterSkillsEntity> expectedSkills = List.of(characterSkillsEntity);
        when(characterSkillsRepository.findByCharacterId(CHARACTER_ID)).thenReturn(expectedSkills);

        List<CharacterSkillsEntity> result = jpaCharacterSkillAdapter.getCharacterIdSkills(CHARACTER_ID);

        assertFalse(result.isEmpty());
        assertEquals(expectedSkills, result);
        verify(characterSkillsRepository, times(1)).findByCharacterId(CHARACTER_ID);
    }
}
