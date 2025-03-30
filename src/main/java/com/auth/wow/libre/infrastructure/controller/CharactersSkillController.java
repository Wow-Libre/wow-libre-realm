package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.character_skills.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/professions")
public class CharactersSkillController {
    private final CharacterSkillsPort characterSkillsPort;

    public CharactersSkillController(CharacterSkillsPort characterSkillsPort) {
        this.characterSkillsPort = characterSkillsPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<CharacterProfessionsModel>>> professions(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = "character_id") final Long characterId,
            @RequestParam(name = "account_id") final Long accountId) {

        List<CharacterProfessionsModel> professions = characterSkillsPort.getProfessions(characterId, accountId,
                transactionId);

        if (professions == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<List<CharacterProfessionsModel>>
                        (transactionId).ok(professions).build());
    }

    @PostMapping("/announcement")
    public ResponseEntity<GenericResponse<Void>> announcement(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid AnnouncementDto request) {

        characterSkillsPort.professionAnnouncement(request.getUserId(), request.getCharacterId(),
                request.getAccountId(), request.getSkillId(), request.getMessage(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>
                        (transactionId).ok().build());
    }

}
