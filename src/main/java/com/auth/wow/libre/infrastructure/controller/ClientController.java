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

        userPort.create(request.getUsername(), request.getPassword(), request.getEmulator(),
                request.getRealmId(), request.getExpansionId(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @DeleteMapping
    public ResponseEntity<GenericResponse<Void>> delete(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USERNAME) final String username) {

        userPort.delete(username, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

}
