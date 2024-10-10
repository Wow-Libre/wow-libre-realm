package com.auth.wow.libre.infrastructure.repositories.characters.mail;

import com.auth.wow.libre.domain.ports.out.mail.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaMailAdapter implements ObtainMail {
    private final MailRepository mailRepository;

    public JpaMailAdapter(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    @Override
    public List<MailEntity> findByMailGuidId(Long characterId, String transactionId) {
        return mailRepository.findByReceiverGuidId(characterId);
    }

    @Override
    public List<MailEntityModel> findByItemsAndMailId(Long mailId, String transactionId) {
        return mailRepository.findByMailsAndItems(mailId);
    }
}
