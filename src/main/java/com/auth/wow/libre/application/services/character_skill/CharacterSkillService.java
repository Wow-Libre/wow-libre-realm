package com.auth.wow.libre.application.services.character_skill;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.character_skills.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.out.character_skills.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CharacterSkillService implements CharacterSkillsPort {

    private final ObtainCharacterSkills obtainCharacterSkills;
    private final CharactersPort charactersPort;

    public CharacterSkillService(ObtainCharacterSkills obtainCharacterSkills, CharactersPort charactersPort) {
        this.obtainCharacterSkills = obtainCharacterSkills;
        this.charactersPort = charactersPort;
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

    private CharacterProfessionsModel mapToModel(CharacterSkillsEntity characterSkills) {

        ProfessionsWow professionsWow = ProfessionsWow.getById(characterSkills.getSkill().intValue());

        return new CharacterProfessionsModel(professionsWow.getId(), professionsWow.getLogo(), professionsWow.name(),
                characterSkills.getValue(), characterSkills.getMax());
    }
}
