package com.auth.wow.libre.domain.exception;

import com.auth.wow.libre.domain.model.exception.*;
import org.junit.jupiter.api.*;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class GenericErrorExceptionTest {

    @Test
    void testGenericErrorExceptionWithAllParams() {
        String message = "Test error";
        String transactionId = "trx-123";
        int code = 400;
        HttpStatus status = HttpStatus.BAD_REQUEST;

        GenericErrorException exception = new GenericErrorException(message, transactionId, code, status);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.transactionId).isEqualTo(transactionId);
        assertThat(exception.code).isEqualTo(code);
        assertThat(exception.httpStatus).isEqualTo(status);
    }

    @Test
    void testGenericErrorExceptionWithHttpStatusOnly() {
        String message = "Unauthorized access";
        String transactionId = "trx-456";
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        GenericErrorException exception = new GenericErrorException(transactionId, message, status);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.transactionId).isEqualTo(transactionId);
        assertThat(exception.code).isEqualTo(status.value());
        assertThat(exception.httpStatus).isEqualTo(status);
    }
}
