package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.character.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaCharactersAdapterTest {
    @Mock
    private CharactersRepository charactersRepository;

    @InjectMocks
    private JpaCharactersAdapter jpaCharactersAdapter;

    @Test
    void getCharactersAndAccountId_ShouldReturnCharacters_WhenExists() {
        Long accountId = 1L;
        CharactersEntity character1 = new CharactersEntity();
        CharactersEntity character2 = new CharactersEntity();
        when(charactersRepository.findByAccount(accountId)).thenReturn(List.of(character1, character2));

        List<CharactersEntity> result = jpaCharactersAdapter.getCharactersAndAccountId(accountId, "txn123");

        assertEquals(2, result.size());
        verify(charactersRepository, times(1)).findByAccount(accountId);
    }

    @Test
    void getCharacter_ShouldReturnCharacter_WhenExists() {
        Long characterId = 10L;
        Long accountId = 1L;
        CharactersEntity character = new CharactersEntity();
        when(charactersRepository.findByGuidAndAccount(characterId, accountId)).thenReturn(Optional.of(character));

        Optional<CharactersEntity> result = jpaCharactersAdapter.getCharacter(characterId, accountId, "txn123");

        assertTrue(result.isPresent());
        verify(charactersRepository, times(1)).findByGuidAndAccount(characterId, accountId);
    }

    @Test
    void count_ShouldReturnTotalCharacters() {
        when(charactersRepository.count()).thenReturn(100L);

        Long result = jpaCharactersAdapter.count();

        assertEquals(100L, result);
        verify(charactersRepository, times(1)).count();
    }

    @Test
    void factions_ShouldReturnFactionStats() {
        FactionsDto factionsDto = new FactionsDto(1L, 2L, 2L);
        when(charactersRepository.characterCountFactions()).thenReturn(factionsDto);

        FactionsDto result = jpaCharactersAdapter.factions("txn123");

        assertNotNull(result);
        verify(charactersRepository, times(1)).characterCountFactions();
    }

    @Test
    void save_ShouldSaveCharacterEntity() {
        CharactersEntity character = new CharactersEntity();
        jpaCharactersAdapter.save(character, "txn123");

        verify(charactersRepository, times(1)).save(character);
    }

    @Test
    void findByAccountAndLevel_ShouldReturnCharacters_WhenExists() {
        Long accountId = 1L;
        int level = 50;
        CharactersEntity character1 = new CharactersEntity();
        CharactersEntity character2 = new CharactersEntity();
        when(charactersRepository.findByAccountAndLevel(accountId, level)).thenReturn(List.of(character1, character2));

        List<CharactersEntity> result = jpaCharactersAdapter.findByAccountAndLevel(accountId, level, "txn123");

        assertEquals(2, result.size());
        verify(charactersRepository, times(1)).findByAccountAndLevel(accountId, level);
    }

    @Test
    void getCharacterId_ShouldReturnCharacter_WhenExists() {
        Long characterId = 10L;
        CharactersEntity character = new CharactersEntity();
        when(charactersRepository.findByGuid(characterId)).thenReturn(Optional.of(character));

        Optional<CharactersEntity> result = jpaCharactersAdapter.getCharacterId(characterId, "txn123");

        assertTrue(result.isPresent());
        verify(charactersRepository, times(1)).findByGuid(characterId);
    }

    @Test
    void getCharactersAvailableMoney_ShouldReturnCharacters_WhenCriteriaMatches() {
        Long accountId = 1L;
        Double money = 5000.0;
        CharactersEntity character1 = new CharactersEntity();
        CharactersEntity character2 = new CharactersEntity();
        when(charactersRepository.findByCharacterAvailableMoney(money, accountId)).thenReturn(List.of(character1,
                character2));

        List<CharactersEntity> result = jpaCharactersAdapter.getCharactersAvailableMoney(accountId, money, "txn123");

        assertEquals(2, result.size());
        verify(charactersRepository, times(1)).findByCharacterAvailableMoney(money, accountId);
    }

    @Test
    void findUserCountsByLevelRange_ShouldReturnLevelRangeStats() {
        LevelRangeDTO levelRangeDTO1 = new LevelRangeDTO("1", 5L);
        LevelRangeDTO levelRangeDTO2 = new LevelRangeDTO("11", 8L);
        when(charactersRepository.findUserCountsByLevelRange()).thenReturn(List.of(levelRangeDTO1, levelRangeDTO2));

        List<LevelRangeDTO> result = jpaCharactersAdapter.findUserCountsByLevelRange("txn123");

        assertEquals(2, result.size());
        verify(charactersRepository, times(1)).findUserCountsByLevelRange();
    }

    @Test
    void findByCharactersByLevel_ShouldReturnCharacters_WhenExists() {
        int level = 60;
        CharactersEntity character1 = new CharactersEntity();
        CharactersEntity character2 = new CharactersEntity();
        when(charactersRepository.findByCharacterOnlineByLevel(level)).thenReturn(List.of(character1, character2));

        List<CharactersEntity> result = jpaCharactersAdapter.findByCharactersByLevel(level, "txn123");

        assertEquals(2, result.size());
        verify(charactersRepository, times(1)).findByCharacterOnlineByLevel(level);
    }
}
