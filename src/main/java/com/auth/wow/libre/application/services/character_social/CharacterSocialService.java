package com.auth.wow.libre.application.services.character_social;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.character_social.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.out.character_social.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
public class CharacterSocialService implements CharacterSocialPort {
    private final CharactersPort charactersPort;
    private final ObtainCharacterSocial obtainCharacterSocial;


    public CharacterSocialService(CharactersPort charactersPort, ObtainCharacterSocial obtainCharacterSocial) {
        this.charactersPort = charactersPort;
        this.obtainCharacterSocial = obtainCharacterSocial;
    }


    @Override
    public CharacterSocialDto getFriends(Long characterId, String transactionId) {

        CharacterDetailDto characterDetail = charactersPort.getCharacter(characterId, transactionId);

        if (characterDetail == null) {
            throw new InternalException("It was not possible to obtain the requested character", transactionId);
        }

        CharacterSocialDto characterSocialDto = new CharacterSocialDto();

        List<CharacterSocial> characterSocials = obtainCharacterSocial.getFriends(characterId, transactionId);

        if (Objects.nonNull(characterSocials)) {

            List<CharacterSocialDetail> friends =
                    characterSocials.stream().map(characterSocial ->
                            Optional.of(charactersPort.getCharacter(characterSocial.friend, transactionId))
                                    .map(character -> new CharacterSocialDetail(character, characterSocial))
                                    .orElse(null)).collect(Collectors.toList());
            characterSocialDto.setFriends(friends);
            characterSocialDto.setTotalQuantity(friends.size());
        }

        return characterSocialDto;
    }

    @Override
    public void deleteFriend(Long characterId, Long accountId, Long friendGuid,
                             String transactionId) {

        if (charactersPort.getCharacter(characterId, accountId, transactionId) == null) {
            throw new InternalException("It was not possible to obtain the requested character", transactionId);
        }

        obtainCharacterSocial.getFriends(characterId, transactionId)
                .stream().filter(characterSocial -> characterSocial.friend.equals(friendGuid))
                .findFirst().orElseThrow(() -> new InternalException("Friend not found", transactionId));

        obtainCharacterSocial.deleteFriend(characterId, friendGuid, transactionId);
    }

    @Override
    public void sendMoney(Long characterId, Long accountId, Long friendGuid, Long money,
                          String transactionId) {

    }

    @Override
    public void sendLevel(Long characterId, Long accountId, Long friendGuid, int level,
                          String transactionId) {

    }
}
