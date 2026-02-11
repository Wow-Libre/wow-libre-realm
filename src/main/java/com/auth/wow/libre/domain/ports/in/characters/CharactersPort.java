package com.auth.wow.libre.domain.ports.in.characters;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;


public interface CharactersPort {
    CharactersDto getCharacters(Long accountId, String transactionId);

    CharactersDto loanApplicationCharacters(Long accountId, int level, int totalTimeSeconds,
                                            String transactionId);

    CharacterDetailDto getCharacter(Long characterId, Long accountId, String transactionId);

    CharacterDetailDto getCharacter(Long characterId, String transactionId);

    void updateMoney(Long characterId, Long amount, String transactionId0);

    List<CharactersEntity> getCharactersAvailableMoney(Long accountId, Double money, String transactionId);

    Long count(String transactionId);

    FactionsDto factions(String transactionId);

    List<LevelRangeDTO> findUserCountsByLevelRange(String transactionId);

    List<CharactersEntity> getCharactersIsLevelMax(Integer level, String transactionId);

    List<CharacterInventoryModel> inventory(Long characterId, Long accountId, String transactionId);

    void transferInventoryItem(Long characterId, Long accountId, Long friendId, Long itemId, Integer count,
                               String emulator, String transactionId);

    void teleport(TeleportDto teleportDto, String transactionId);

    boolean updateStatsCharacter(UpdateStatsRequest request, String emulator, String transactionId);
}
