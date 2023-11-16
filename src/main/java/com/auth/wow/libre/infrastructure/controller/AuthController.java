package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.AccountLoginDto;
import com.auth.wow.libre.domain.model.security.JwtDto;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.GenericResponseBuilder;
import com.auth.wow.libre.domain.ports.in.account.AuthPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthPort authPort;

  public AuthController(AuthPort authPort) {
    this.authPort = authPort;
  }


  @PostMapping(path = "login")
  public ResponseEntity<GenericResponse<JwtDto>> login(
          @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
          @RequestBody @Valid AccountLoginDto account) {

    JwtDto jwt = authPort.login(account.getUsername(), account.getPassword(), transactionId);

    return ResponseEntity
            .status(HttpStatus.OK)
            .body(new GenericResponseBuilder<>(jwt, transactionId).ok().build());
  }


}
