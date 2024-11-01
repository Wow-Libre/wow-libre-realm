package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.bank.*;
import jakarta.validation.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

@RestController
@RequestMapping("/api/bank")
public class BankController {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(BankController.class);
    private final BankPort bankPort;

    public BankController(BankPort bankPort) {
        this.bankPort = bankPort;
    }

    @PutMapping(path = "/payment")
    public ResponseEntity<GenericResponse<Double>> payment(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid BankPaymentDto request) {
        LOGGER.error("{}",request.getAmount());
        Double remainingMoneyToPay = bankPort.collectGold(request.getUserId(), request.getAmount(),
                transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(remainingMoneyToPay, transactionId).created().build());
    }

}
