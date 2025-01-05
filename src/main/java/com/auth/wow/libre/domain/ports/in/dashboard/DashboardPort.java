package com.auth.wow.libre.domain.ports.in.dashboard;

import com.auth.wow.libre.domain.model.dto.*;

public interface DashboardPort {
    DashboardMetricsDto metricsCount(String transactionId);

    void updateMailAccount(String username, String updateMail, String transactionId);
}
