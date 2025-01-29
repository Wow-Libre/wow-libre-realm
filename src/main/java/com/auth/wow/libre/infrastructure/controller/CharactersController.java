package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/characters")
public class CharactersController {
    private final CharactersPort charactersPort;

    public CharactersController(CharactersPort charactersPort) {
        this.charactersPort = charactersPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<CharactersDto>> characters(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId) {

        CharactersDto characters = charactersPort.getCharacters(accountId, transactionId);

        if (characters != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<CharactersDto>
                            (transactionId).ok(characters).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/loan/bank")
    public ResponseEntity<GenericResponse<CharactersDto>> loanApplicationCharacters(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId,
            @RequestParam int time,
            @RequestParam int level) {

        CharactersDto characters = charactersPort.loanApplicationCharacters(accountId, level, time, transactionId);

        if (characters != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<CharactersDto>
                            (transactionId).ok(characters).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{character_id}")
    public ResponseEntity<GenericResponse<CharacterDetailDto>> character(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId,
            @PathVariable(name = "character_id") final Long characterId) {

        CharacterDetailDto character = charactersPort.getCharacter(characterId, accountId, transactionId);

        if (character != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponseBuilder<CharacterDetailDto>(transactionId)
                    .ok(character).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{character_id}/inventory")
    public ResponseEntity<GenericResponse<List<CharacterInventoryModel>>> inventory(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId,
            @PathVariable(name = "character_id") final Long characterId) {

        List<CharacterInventoryModel> inventory = charactersPort.inventory(characterId, accountId, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<List<CharacterInventoryModel>>(transactionId)
                        .ok(inventory).build());
    }

    @PostMapping("/inventory/transfer")
    public ResponseEntity<GenericResponse<Void>> transferInventoryItem(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid TransferItemRequest request) {

        charactersPort.transferInventoryItem(request.getCharacterId(),
                request.getAccountId(), request.getFriendId(), request.getItemId(), request.getQuantity(),
                transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId)
                        .ok().build());
    }
}
