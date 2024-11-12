package com.auth.wow.libre.application.services.character_skill;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.character_service.*;
import com.auth.wow.libre.domain.ports.in.character_skills.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.out.character_skills.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class CharacterSkillService implements CharacterSkillsPort {

    private final ObtainCharacterSkills obtainCharacterSkills;
    private final CharactersPort charactersPort;
    private final CharacterTransactionPort characterTransactionPort;

    public CharacterSkillService(ObtainCharacterSkills obtainCharacterSkills, CharactersPort charactersPort,
                                 CharacterTransactionPort characterTransactionPort) {
        this.obtainCharacterSkills = obtainCharacterSkills;
        this.charactersPort = charactersPort;
        this.characterTransactionPort = characterTransactionPort;
    }


    @Override
    public List<CharacterProfessionsModel> getProfessions(Long characterId, Long accountId, String transactionId) {

        if (charactersPort.getCharacter(characterId, accountId, transactionId) == null) {
            throw new InternalException("The character with the associated account could not be found.", transactionId);
        }

        return obtainCharacterSkills.getCharacterIdSkills(characterId).stream()
                .filter(filterProfession -> ProfessionsWow.getById(filterProfession.getSkill().intValue()) != null)
                .map(this::mapToModel).toList();
    }

    @Override
    public void professionAnnouncement(Long userId, Long characterId, Long accountId, Long skillId,
                                       String transactionId) {

        CharacterDetailDto characterDetailDto = charactersPort.getCharacter(characterId, accountId, transactionId);

        if (characterDetailDto == null) {
            throw new InternalException("The character with the associated account could not be found.", transactionId);
        }

        Optional<CharacterProfessionsModel> characterSkills =
                obtainCharacterSkills.getCharacterIdSkills(characterId).stream()
                        .filter(profession -> profession.getSkill().equals(skillId))
                        .filter(filterProfession -> ProfessionsWow.getById(filterProfession.getSkill().intValue()) != null)
                        .map(this::mapToModel).findFirst();

        if (characterSkills.isEmpty()) {
            throw new InternalException("There is no profession for this character.", transactionId);
        }

        characterTransactionPort.create(CharacterTransactionModel.builder()
                .successful(true)
                .userId(userId)
                .characterId(characterId)
                .accountId(accountId)
                .command(CommandsCore.announcement(String.format("Specialty: %s  Character: %s Lvl: %s",
                        characterSkills.get().name(), characterDetailDto.getName(), characterSkills.get().value()
                )))
                .transactionDate(LocalDateTime.now())
                .status(true)
                .indebtedness(true)
                .amount(TransactionType.ANNOUNCEMENT.getCost())
                .transactionType(TransactionType.ANNOUNCEMENT)
                .build(), transactionId);

    }

    private CharacterProfessionsModel mapToModel(CharacterSkillsEntity characterSkills) {

        ProfessionsWow professionsWow = ProfessionsWow.getById(characterSkills.getSkill().intValue());

        return new CharacterProfessionsModel(professionsWow.getId(), professionsWow.getLogo(), professionsWow.name(),
                characterSkills.getValue(), characterSkills.getMax());
    }
}
