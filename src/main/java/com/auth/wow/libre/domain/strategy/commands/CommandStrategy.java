package com.auth.wow.libre.domain.strategy.commands;

public interface CommandStrategy {
    String getCreateCommand(String username, String password);

    String getChangePasswordCommand(String username, String password);

    String getChangePasswordBattleNetCommand(String username, String password);
}
