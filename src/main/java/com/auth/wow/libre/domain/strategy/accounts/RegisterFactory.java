package com.auth.wow.libre.domain.strategy.accounts;

import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.out.account.*;

public class RegisterFactory {

    public static Account getExpansion(Integer expansion, ExecuteCommandsPort executeCommandsPort,
                                       ObtainAccountPort obtainAccountPort,
                                       SaveAccountPort saveAccountPort) {
        return switch (expansion) {
            case 10 -> new AccountWarWithin(executeCommandsPort, obtainAccountPort, saveAccountPort);
            case 2 -> new AccountLK(executeCommandsPort, obtainAccountPort, saveAccountPort);
            default -> new AccountLK(executeCommandsPort, obtainAccountPort, saveAccountPort);
        };
    }
}
