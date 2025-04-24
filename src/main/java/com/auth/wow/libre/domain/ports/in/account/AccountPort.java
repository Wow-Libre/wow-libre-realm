package com.auth.wow.libre.domain.ports.in.account;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account.*;

public interface AccountPort {
    Long create(String username, String password, String email, Long userId,
                Integer expansion, byte[] salt, String transactionId);

    void createUser(String username, String password, String email, String recaptchaResponse,
                    Integer expansionId, String transactionId);

    Long countOnline(String transactionId);

    Boolean isOnline(Long accountId, String transactionId);

    AccountDetailDto account(Long accountId, String transactionId);

    void changePassword(Long accountId, Long userId, String password, byte[] saltPassword, Integer expansionId,
                        String transactionId);

    AccountsDto accounts(int size, int page, String filter, String transactionId);

    Long count(String transactionId);

    MetricsProjection metrics(String transactionId);

    void updateMail(String username, String updateMail, String transactionId);

    void bannedUser(String username, Integer days, Integer hours, Integer minutes, Integer seconds, String bannedBy,
                    String banReason, String transactionId);

    void changePassword(String username, String password, String newPassword, String transactionId);

}
