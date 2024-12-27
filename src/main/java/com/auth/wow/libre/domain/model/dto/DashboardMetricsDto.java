package com.auth.wow.libre.domain.model.dto;

import lombok.*;

@Data
public class DashboardMetricsDto {
    private Long totalUsers;
    private Long onlineUsers;
    private Long totalGuilds;
    private Long externalRegistrations;
    private Long characterCount;
}
