package com.auth.wow.libre.infrastructure.repositories.auth.account;

import lombok.*;

@Data
@AllArgsConstructor
public class MetricsProjection {
    private Long totalUsers;
    private Long onlineUsers;
    private Long totalUsersExternal;
}
