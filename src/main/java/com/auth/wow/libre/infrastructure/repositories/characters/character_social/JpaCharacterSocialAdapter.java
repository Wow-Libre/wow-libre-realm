package com.auth.wow.libre.infrastructure.repositories.characters.character_social;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.out.character_social.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Repository
public class JpaCharacterSocialAdapter implements ObtainCharacterSocial {
    private final CharacterSocialRepository characterSocialRepository;

    public JpaCharacterSocialAdapter(CharacterSocialRepository characterSocialRepository) {
        this.characterSocialRepository = characterSocialRepository;
    }

    @Override
    public List<CharacterSocial> getFriends(Long guid, String transactionId) {
        return characterSocialRepository.findByGuid(guid).stream().map(this::mapToModel).collect(Collectors.toList());
    }

    @Override
    public void deleteFriend(Long guid, Long friendGuid, String transactionId) {
        characterSocialRepository.findByGuid(guid).stream()
                .filter(characterSocialEntity -> characterSocialEntity.getFriend().equals(friendGuid))
                .findFirst().ifPresent(characterSocialRepository::delete);
    }

    private CharacterSocial mapToModel(CharacterSocialEntity characterSocialEntity) {
        return new CharacterSocial(characterSocialEntity.getGuid(),
                characterSocialEntity.getFriend(), characterSocialEntity.getFlags(),
                characterSocialEntity.getNote());
    }
}
