package com.auth.wow.libre.infrastructure.repositories.characters.character;

import com.auth.wow.libre.domain.ports.out.characters.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaCharactersAdapter implements ObtainCharacters, SaveCharacters {
    private final CharactersRepository charactersRepository;

    public JpaCharactersAdapter(CharactersRepository charactersRepository) {
        this.charactersRepository = charactersRepository;
    }

    @Override
    public List<CharactersEntity> getCharactersAndAccountId(Long accountId, String transactionId) {
        return charactersRepository.findByAccount(accountId);
    }

    @Override
    public List<CharactersEntity> findByAccountAndLevel(Long accountId, int level, String transactionId) {
        return charactersRepository.findByAccountAndLevel(accountId, level);
    }

    @Override
    public Optional<CharactersEntity> getCharacter(Long characterId, Long accountId, String transactionId) {
        return charactersRepository.findByGuidAndAccount(characterId, accountId);
    }

    @Override
    public Optional<CharactersEntity> getCharacterId(Long characterId, String transactionId) {
        return charactersRepository.findByGuid(characterId);
    }

    @Override
    public List<CharactersEntity> getCharactersAvailableMoney(Long accountId, Double money, String transactionId) {
        return charactersRepository.findByCharacterAvailableMoney(money, accountId);
    }

    @Override
    public void save(CharactersEntity characters, String transactionId) {
        charactersRepository.save(characters);
    }
}
