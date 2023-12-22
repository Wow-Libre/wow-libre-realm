package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.model.UpdateAccount;
import com.auth.wow.libre.domain.model.dto.AccountDto;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.GenericResponseBuilder;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;
import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_USERNAME_JWT;

@RestController
@RequestMapping("/api/account")
public class AccountController {

  private final AccountPort accountPort;

  public AccountController(AccountPort accountPort) {
    this.accountPort = accountPort;
  }


  @PostMapping
  public ResponseEntity<GenericResponse<Void>> create(
          @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
          @RequestBody @Valid AccountDto account) {
    accountPort.create(account, transactionId);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new GenericResponseBuilder<Void>(transactionId).created().build());
  }

  @GetMapping(path = "/user")
  public ResponseEntity<GenericResponse<Account>> get(
          @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
          @RequestHeader(name = HEADER_USERNAME_JWT) final String username) {
    Account accountFound = accountPort.obtain(username, transactionId);

    if (accountFound != null) {
      return ResponseEntity
              .status(HttpStatus.OK)
              .body(new GenericResponseBuilder<Account>(transactionId).ok(accountFound).build());
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PutMapping
  public ResponseEntity<Void> update(
          @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
          @RequestBody @Valid UpdateAccount updateAccount,
          @RequestHeader(name = HEADER_USERNAME_JWT) final String username) {
    accountPort.updated(username, updateAccount, transactionId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }


}
