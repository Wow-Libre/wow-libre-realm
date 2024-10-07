package com.auth.wow.libre.domain.model;

import lombok.*;

import java.util.*;

public class MailModel {
    public final Long id;
    public final Long messageType;
    public final Long senderGuidId;
    public final String senderName;
    public final String subject;
    public final String body;
    public final boolean hasItems;
    public final Date expireTime;
    public final Date deliverTime;
    public final Integer money;
    public final List<Items> items;

    public MailModel(Long id, Long messageType, Long senderGuidId, String senderName, String subject, String body,
                     boolean hasItems, Date expireTime, Date deliverTime, Integer money, List<Items> items
    ) {
        this.id = id;
        this.messageType = messageType;
        this.senderGuidId = senderGuidId;
        this.senderName = senderName;
        this.subject = subject;
        this.body = body;
        this.hasItems = hasItems;
        this.expireTime = expireTime;
        this.deliverTime = deliverTime;
        this.money = money;
        this.items = items;
    }

    @Data
    @AllArgsConstructor
    public static class Items {
        private Long mailId;
        private Long itemId;
        private Long duration;
        private Long itemInstanceId;
    }
}
