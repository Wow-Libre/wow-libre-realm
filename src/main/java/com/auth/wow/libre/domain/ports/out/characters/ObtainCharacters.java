package com.auth.wow.libre.domain.ports.out.characters;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ObtainCharacters {
    List<CharactersEntity> getCharactersAndAccountId(Long accountId, String transactionId);

    List<CharactersEntity> findByAccountAndLevel(Long accountId, int level, String transactionId);

    Optional<CharactersEntity> getCharacter(Long characterId, Long accountId, String transactionId);

    Optional<CharactersEntity> getCharacterId(Long characterId, String transactionId);

    List<CharactersEntity> getCharactersAvailableMoney(Long accountId, Double money, String transactionId);

    Long count();

    FactionsDto factions(String transactionId);

    List<LevelRangeDTO> findUserCountsByLevelRange(String transactionId);

    List<CharactersEntity> findByCharactersByLevel(int level, String transactionId);

}
