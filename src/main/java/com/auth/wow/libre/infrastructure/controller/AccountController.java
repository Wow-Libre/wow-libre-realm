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
                request.getUserId(), request.getExpansionId(), request.getSalt(), transactionId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<>(accountId, transactionId).created().build());
    }


    @GetMapping(path = "/{account_id}")
    public ResponseEntity<GenericResponse<AccountDetailDto>> account(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable final Long account_id) {

        final AccountDetailDto account = accountPort.account(account_id, transactionId);

        if (account != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<AccountDetailDto>(transactionId).ok(account).build());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity<GenericResponse<Void>> changePassword(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid ChangePasswordAccountDto request) {

        accountPort.changePassword(request.getAccountId(), request.getUserId(), request.getPassword(),
                request.getSalt(), request.getExpansionId(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }


    @GetMapping(path = "/all")
    public ResponseEntity<GenericResponse<AccountsDto>> accounts(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam final int size,
            @RequestParam final int page,
            @RequestParam final String filter) {

        final AccountsDto accounts = accountPort.accounts(size, page, filter, transactionId);

        if (accounts != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<AccountsDto>(transactionId).ok(accounts).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
