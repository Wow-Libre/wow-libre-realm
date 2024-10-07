package com.auth.wow.libre.domain.ports.in.mail;

import com.auth.wow.libre.domain.model.dto.*;

public interface MailPort {
    MailsDto getMails(Long characterId, String transactionId);

}
