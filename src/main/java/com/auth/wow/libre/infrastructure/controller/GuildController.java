package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.guild.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/guilds")
public class GuildController {

    private final GuildPort guildPort;

    public GuildController(GuildPort guildPort) {
        this.guildPort = guildPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<GuildsDto>> guilds(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam final Integer size,
            @RequestParam final Integer page,
            @RequestParam final String search) {
        GuildsDto guilds = guildPort.findAll(size, page, search, transactionId);

        if (guilds == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<GuildsDto>(transactionId).ok(guilds).build());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GenericResponse<GuildDto>> guild(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable final Long id) {

        GuildDto guild = guildPort.detail(id, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<GuildDto>(transactionId).ok(guild).build());
    }

    @PutMapping(path = "/{guild_id}/attach")
    public ResponseEntity<GenericResponse<Void>> attach(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = "account_id") final Long accountId,
            @PathVariable(name = "guild_id") final Long guildId,
            @RequestParam(name = "character_id") final Long characterId) {

        guildPort.attach(guildId, accountId, characterId, transactionId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


    @DeleteMapping(path = "/member/{character_id}")
    public ResponseEntity<GenericResponse<Void>> unInviteGuild(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable(name = "character_id") final Long characterId,
            @RequestParam(name = "account_id") final Long accountId) {

        guildPort.unInviteGuild(accountId, characterId, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


}
