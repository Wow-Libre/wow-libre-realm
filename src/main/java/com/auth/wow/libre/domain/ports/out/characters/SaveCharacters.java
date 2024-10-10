package com.auth.wow.libre.domain.ports.out.characters;

import com.auth.wow.libre.infrastructure.entities.characters.*;

public interface SaveCharacters {
    void save(CharactersEntity characters, String transactionId);
}
