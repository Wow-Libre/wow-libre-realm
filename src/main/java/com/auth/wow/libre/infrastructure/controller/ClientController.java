package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.client.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

@RestController
@RequestMapping("/api/client")
public class ClientController {
    private final ClientPort clientPort;

    public ClientController(ClientPort clientPort) {
        this.clientPort = clientPort;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Void>> create(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid CreateClientDto request) {

        clientPort.create(request.getUsername(), request.getPassword(), request.getSalt(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }
}
