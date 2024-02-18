package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.AccountDetail;
import com.auth.wow.libre.domain.model.dto.AccountDto;
import com.auth.wow.libre.domain.model.dto.ChangePasswordAccountDto;
import com.auth.wow.libre.domain.model.dto.UpdateAccountDto;
import com.auth.wow.libre.domain.model.dto.WebPasswordAccountDto;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.GenericResponseBuilder;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import jakarta.validation.Valid;
import org.apache.commons.codec.DecoderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<GenericResponse<AccountDetail>> get(
      @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
      @RequestHeader(name = HEADER_USERNAME_JWT) final String username) {
    AccountDetail accountFound = accountPort.obtain(username, transactionId);

    if (accountFound != null) {
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(new GenericResponseBuilder<AccountDetail>(transactionId).ok(accountFound).build());
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PutMapping
  public ResponseEntity<Void> update(
      @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
      @RequestBody @Valid UpdateAccountDto updateAccountDto,
      @RequestHeader(name = HEADER_USERNAME_JWT) final String username) {
    accountPort.updated(username, updateAccountDto, transactionId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping(path = "/password/change/game")
  public ResponseEntity<Void> gameChangeKey(
      @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
      @RequestBody @Valid ChangePasswordAccountDto changePasswordAccountDto,
      @RequestHeader(name = HEADER_USERNAME_JWT) final String username) throws DecoderException {
    accountPort.gameChangePassword(username, changePasswordAccountDto, transactionId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }


  @PostMapping(path = "/password/change/web")
  public ResponseEntity<Void> webChangeKey(
      @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
      @RequestBody @Valid WebPasswordAccountDto webPasswordAccountDto,
      @RequestHeader(name = HEADER_USERNAME_JWT) final String username) {

    accountPort.webChangePassword(username, webPasswordAccountDto, transactionId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
