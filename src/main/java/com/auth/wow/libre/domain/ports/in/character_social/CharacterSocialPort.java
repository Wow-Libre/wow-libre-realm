package com.auth.wow.libre.domain.ports.in.character_social;

import com.auth.wow.libre.domain.model.dto.*;

public interface CharacterSocialPort {
    CharacterSocialDto getFriends(Long characterId, String transactionId);

    void deleteFriend(Long characterId, Long accountId, Long friendGuid, Long userId, String transactionId);

    void sendMoney(Long characterId, Long accountId, Long userId, Long friendGuid, Long money, Double cost,
                   String transactionId);

    void sendLevel(Long characterId, Long accountId, Long userId, Long friendGuid, int level, Double cost,
                   String transactionId);
}
