package com.auth.wow.libre.domain.ports.out.mail;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.mail.*;

import java.util.*;

public interface ObtainMail {
    List<MailEntity> findByMailGuidId(Long guidId, String transactionId);

    List<MailEntityDto> findByItemsAndMailId(Long mailId, String transactionId);
}
