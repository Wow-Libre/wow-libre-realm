package com.auth.wow.libre.domain.ports.in.characters;

import com.auth.wow.libre.domain.model.dto.*;

public interface CharactersPort {
    CharactersDto getCharacters(Long accountId, String transactionId);

    CharacterDetailDto getCharacter(Long characterId, Long accountId, String transactionId);

    CharacterDetailDto getCharacter(Long characterId, String transactionId);


}
