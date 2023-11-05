package com.auth.wow.libre.application.services.account;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.ports.in.account.AccountPort;
import com.auth.wow.libre.domain.ports.out.account.LoadAccountPort;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements AccountPort {

    private final LoadAccountPort loadAccountPort;
    private final ObtainAccountPort obtainAccountPort;

    public AccountService(LoadAccountPort loadAccountPort, ObtainAccountPort obtainAccountPort) {
        this.loadAccountPort = loadAccountPort;
        this.obtainAccountPort = obtainAccountPort;
    }

    @Override
    public void create(Account account) {

        if (obtainAccountPort.findByUsername(account.getUsername()) != null) {
            throw new RuntimeException("El cliente ya existe");
        }

        loadAccountPort.save(account);
    }

    @Override
    public Account Obtain(String username) {
        return obtainAccountPort.findByUsername(username);
    }
}
