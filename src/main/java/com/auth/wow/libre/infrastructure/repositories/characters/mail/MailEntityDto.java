package com.auth.wow.libre.infrastructure.repositories.characters.mail;

import lombok.*;

@Data
public class MailEntityDto {
    private Long mailId;
    private Long characterReceiverId;
    private Long itemId;
    private Long duration;
    private Long itemInstanceId;

    public MailEntityDto(Long mailId, Long characterReceiverId, Long itemId, Long duration, Long itemInstanceId) {
        this.mailId = mailId;
        this.characterReceiverId = characterReceiverId;
        this.itemId = itemId;
        this.duration = duration;
        this.itemInstanceId = itemInstanceId;
    }
}
