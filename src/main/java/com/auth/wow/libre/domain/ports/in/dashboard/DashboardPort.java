package com.auth.wow.libre.domain.ports.in.dashboard;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.dto.view.*;

import java.util.*;

public interface DashboardPort {
    DashboardMetricsDto metricsCount(String transactionId);

    void updateMailAccount(String username, String updateMail, String transactionId);

    void bannedUser(AccountBanDto banDto, String transactionId);

    Map<String, String> getFileConfig(String rute, String transactionId);

    void updateFileConfig(String originalFilePath, Map<String, String> replacements, String transactionId);

    List<Card> findByPublications(String transactionId);
}
