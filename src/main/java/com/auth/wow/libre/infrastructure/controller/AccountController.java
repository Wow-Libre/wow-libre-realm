package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.GenericResponseBuilder;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

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
          @RequestBody @Valid Account account) {
    accountPort.create(account);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new GenericResponseBuilder<Void>(transactionId).created().build());
  }

  @GetMapping(path = "/user")
  public ResponseEntity<GenericResponse<Account>> get(
          @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
          @RequestParam String username) {
    Account accountFound = accountPort.Obtain(username);

    if (accountFound != null) {
      return ResponseEntity
              .status(HttpStatus.OK)
              .body(new GenericResponseBuilder<Account>(transactionId).ok(accountFound).build());
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


}
