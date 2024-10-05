package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountPort accountPort;

    public AccountController(AccountPort accountPort) {
        this.accountPort = accountPort;
    }


    @PostMapping(path = "/create")
    public ResponseEntity<GenericResponse<Long>> create(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid CreateAccountDto request) {

        Long accountId = accountPort.create(request.getUsername(), request.getPassword(), request.getEmail(),
                request.isRebuildUsername(), request.getUserId(), request.getExpansion(), request.getSalt(),
                transactionId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<>(accountId, transactionId).created().build());
    }


}
