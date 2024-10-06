package com.auth.wow.libre.domain.ports.in.characters;

import com.auth.wow.libre.domain.model.dto.*;

public interface CharactersPort {
    CharactersDto getCharacters(Long accountId, Long userId, String transactionId);

    CharacterDetailDto getCharacter(Long characterId, Long accountId, Long userId, String transactionId);

}
