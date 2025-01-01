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

}
