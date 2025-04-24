package com.auth.wow.libre.domain.strategy.commands;

public class TrinityWarWithinCommandStrategy implements CommandStrategy {
    @Override
    public String getCreateCommand(String email, String password) {
        return String.format("bnetaccount create %s %s", email, password);
    }

    @Override
    public String getChangePasswordCommand(String username, String password) {
        return String.format("account set password %s %s  %s", username, password, password);
    }

    @Override
    public String getChangePasswordBattleNetCommand(String username, String password) {
        return String.format("bnetaccount set password %s %s  %s", username, password, password);
    }
}
