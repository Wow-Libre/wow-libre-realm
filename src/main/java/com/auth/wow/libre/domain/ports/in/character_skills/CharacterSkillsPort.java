package com.auth.wow.libre.domain.ports.in.character_skills;

import com.auth.wow.libre.domain.model.*;

import java.util.*;

public interface CharacterSkillsPort {
    List<CharacterProfessionsModel> getProfessions(Long characterId, Long accountId, String transactionId);

    void professionAnnouncement(Long userId, Long characterId, Long accountId, Long skillId,
                                String transactionId);
}
