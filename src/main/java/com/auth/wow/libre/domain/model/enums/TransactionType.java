package com.auth.wow.libre.domain.model.enums;

import lombok.*;

@Getter
@AllArgsConstructor
public enum TransactionType {
    ANNOUNCEMENT("announcement", 10000000.0),
    BANK("bank", 0.0),
    SEND_MONEY("send_money", 10000.0),
    SEND_LEVEL("send_level", 50000000.0),
    SEND_ITEMS("send_items", 0.0),;

    private final String description;
    private final Double cost;
}
