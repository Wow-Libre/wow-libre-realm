package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.user.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/client")
public class ClientController {
    private final UserPort userPort;

    public ClientController(UserPort userPort) {
        this.userPort = userPort;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Void>> create(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid CreateClientDto request) {

        userPort.create(request.getUsername(), request.getPassword(), request.getSalt(), request.getEmulator(),
                request.getApiKey(), request.getExpansionId(), request.getGmUsername(), request.getGmPassword(),
                transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }
}
