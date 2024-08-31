package com.auth.wow.libre.infrastructure.controller.external;

import com.auth.wow.libre.domain.model.comunication.MailSenderVars;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.GenericResponseBuilder;
import com.auth.wow.libre.infrastructure.conf.comunication.EmailSend;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sierra")
public class SierraCodeController {
    private final EmailSend emailSend;

    public SierraCodeController(EmailSend emailSend) {
        this.emailSend = emailSend;
    }

    @PostMapping(path = "/")
    public ResponseEntity<GenericResponse<Void>> game(@RequestBody SierraCodeEmails request) {

        request.emails.stream().forEach( email ->{

            emailSend.sendHTMLEmail(email, "Revitaliza tu presencia online: Actualización web a la medida de tu institución",
                    MailSenderVars.builder().email(email).transactionId("").build());
        });

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<Void>("").created().build());
    }
}
