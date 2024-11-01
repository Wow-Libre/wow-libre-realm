package com.auth.wow.libre.domain.model.exception;

import org.springframework.http.HttpStatus;

public class GenericErrorException extends RuntimeException {
    public final String transactionId;
    public final Integer code;
    public final HttpStatus httpStatus;

    public GenericErrorException( String message, String transactionId, Integer code, HttpStatus httpStatus) {
        super(message);
        this.transactionId = transactionId;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public GenericErrorException(String transactionId, String message, HttpStatus httpStatus) {
        super(message);
        this.transactionId = transactionId;
        this.code = httpStatus.value();
        this.httpStatus = httpStatus;
    }
}
