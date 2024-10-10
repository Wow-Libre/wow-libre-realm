package com.auth.wow.libre.application.services.mail;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.mail.*;
import com.auth.wow.libre.domain.ports.out.mail.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.mail.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class MailService implements MailPort {
    private final ObtainMail obtainMail;
    private final CharactersPort charactersPort;

    public MailService(ObtainMail obtainMail, CharactersPort charactersPort) {
        this.obtainMail = obtainMail;
        this.charactersPort = charactersPort;
    }

    @Override
    public MailsDto getMails(Long characterId, String transactionId) {
        final List<MailModel> mails = obtainMail.findByMailGuidId(characterId, transactionId).stream()
                .map(mail -> mapToModel(mail, transactionId)).toList();

        return new MailsDto(mails, mails.size());
    }

    private MailModel mapToModel(MailEntity mail, String transactionId) {
        Date deliverTime = Date.from(Instant.ofEpochMilli(mail.getDeliverTime() * 1000));
        Date expirationMail = Date.from(Instant.ofEpochMilli(mail.getExpireTime() * 1000));
        final CharacterDetailDto characterSender = charactersPort.getCharacter(mail.getSenderGuidId(), transactionId);

        return new MailModel(mail.getId(), mail.getMessageType(), mail.getSenderGuidId(),
                Optional.ofNullable(characterSender).map(CharacterDetailDto::getName).orElse("Wow Libre"),
                mail.getSubject(), mail.getBody(), mail.isHasItems(), expirationMail, deliverTime,
                mail.getMoney(), mail.isHasItems()
                ? obtainMail.findByItemsAndMailId(mail.getId(), transactionId).stream().map(this::mapToModel).toList()
                : null);
    }

    private MailModel.Items mapToModel(MailEntityModel mailDto) {
        return new MailModel.Items(mailDto.getMailId(), mailDto.getItemId(), mailDto.getDuration(),
                mailDto.getItemInstanceId());
    }
}
