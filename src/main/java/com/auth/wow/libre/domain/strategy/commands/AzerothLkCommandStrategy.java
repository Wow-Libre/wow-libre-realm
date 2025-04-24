package com.auth.wow.libre.domain.strategy.commands;

public class AzerothLkCommandStrategy implements CommandStrategy {
    @Override
    public String getCreateCommand(String username, String password) {
        return String.format("account create %s %s", username, password);
    }

    @Override
    public String getChangePasswordCommand(String username, String password) {
        return String.format("account set password %s %s %s", username, password, password);
    }

    @Override
    public String getChangePasswordBattleNetCommand(String username, String password) {
        return "";
    }
}
