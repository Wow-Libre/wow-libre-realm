package com.auth.wow.libre.domain.model.comunication;

import lombok.Builder;

@Builder
public class MailSenderVars <T> {
    public final String emailFrom;
    public final String subject;
    public final Integer idTemplate;
    public final String transactionId;
    public final T data;
}
