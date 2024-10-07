package com.auth.wow.libre.domain.ports.in.character_social;

import com.auth.wow.libre.domain.model.dto.*;

public interface CharacterSocialPort {
    CharacterSocialDto getFriends(Long characterId, String transactionId);

    void deleteFriend(Long characterId, Long accountId, Long friendGuid, String transactionId);

    void sendMoney(Long characterId, Long accountId, Long friendGuid, Long money,
                   String transactionId);

    void sendLevel(Long characterId, Long accountId, Long friendGuid, int level,
                   String transactionId);
}
