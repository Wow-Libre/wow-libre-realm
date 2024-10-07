package com.auth.wow.libre.domain.ports.out.character_social;

import com.auth.wow.libre.domain.model.*;

import java.util.*;

public interface ObtainCharacterSocial {
    List<CharacterSocial> getFriends(Long characterId, String transactionId);

    void deleteFriend(Long characterId, Long friendGuid, String transactionId);
}
