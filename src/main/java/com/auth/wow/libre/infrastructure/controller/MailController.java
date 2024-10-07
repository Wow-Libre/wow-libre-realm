package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.mail.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/mails")
public class MailController {

    private final MailPort mailPort;

    public MailController(MailPort mailPort) {
        this.mailPort = mailPort;
    }

    @GetMapping("/{character_id}")
    public ResponseEntity<GenericResponse<MailsDto>> mails(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable(name = "character_id") final Long characterId) {

        MailsDto mails = mailPort.getMails(characterId, transactionId);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponseBuilder<MailsDto>(transactionId)
                .ok(mails).build());
    }
}
