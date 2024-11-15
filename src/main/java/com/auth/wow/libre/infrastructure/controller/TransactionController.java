package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.transaction.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionPort transactionPort;

    public TransactionController(TransactionPort transactionPort) {
        this.transactionPort = transactionPort;
    }


    @PostMapping("/purchase")
    public ResponseEntity<GenericResponse<Void>> sendItems(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid CreateTransactionItemsDto request) {

        transactionPort.sendItems(request.getUserId(), request.getAccountId(), request.getItems(),
                request.getReference(), request.getAmount(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }

    @PostMapping("/subscription-benefits")
    public ResponseEntity<GenericResponse<Void>> subscriptionBenefits(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid SubscriptionBenefitsDto request) {

        transactionPort.sendSubscriptionBenefits(request.getUserId(), request.getAccountId(),
                request.getCharacterId(), request.getItems(),
                request.getBenefitType(), request.getAmount(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }
}
