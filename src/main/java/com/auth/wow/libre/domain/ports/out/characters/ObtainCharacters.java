package com.auth.wow.libre.domain.ports.out.characters;

import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ObtainCharacters {
    List<CharactersEntity> getCharacters(Long accountId, String transactionId);

    Optional<CharactersEntity> getCharacter(Long characterId, Long accountId, String transactionId);
}
