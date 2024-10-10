package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.character_social.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/social")
public class CharactersSocialController {
    private final CharacterSocialPort characterSocialPort;

    public CharactersSocialController(CharacterSocialPort characterSocialPort) {
        this.characterSocialPort = characterSocialPort;
    }

    @DeleteMapping("/{character_id}/{friendGuid}")
    public ResponseEntity<GenericResponse<Void>> deleteFriend(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable final Long friendGuid,
            @PathVariable(name = "character_id") final Long characterId,
            @RequestParam(name = "account_id") final Long account_id,
            @RequestParam(name = "user_id") final Long userId) {

        characterSocialPort.deleteFriend(characterId, account_id, friendGuid, userId, transactionId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{character_id}/friends")
    public ResponseEntity<GenericResponse<CharacterSocialDto>> friends(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable(name = "character_id") final Long characterId) {

        CharacterSocialDto character = characterSocialPort.getFriends(characterId, transactionId);

        if (character != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new GenericResponseBuilder<CharacterSocialDto>
                            (transactionId).ok(character).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/send/money")
    public ResponseEntity<GenericResponse<CharactersDto>> sendMoney(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid SendMoneyDto request) {

        characterSocialPort.sendMoney(request.getCharacterId(), request.getAccountId(), request.getUserId(),
                request.getFriendId(), request.getMoney(), request.getCost(), transactionId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/send/level")
    public ResponseEntity<GenericResponse<CharactersDto>> sendLevel(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid SendLevelDto request) {

        characterSocialPort.sendLevel(request.getCharacterId(), request.getAccountId(), request.getUserId(),
                request.getFriendId(), request.getLevel(), request.getCost(), transactionId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
