package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.ports.in.account.CreateAccountPort;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    private final CreateAccountPort createAccountPort;

    public RegisterController(CreateAccountPort createAccountPort) {
        this.createAccountPort = createAccountPort;
    }


    @PostMapping
    public void account(@RequestBody @Valid Account account) {
        createAccountPort.create(account);
    }
}
