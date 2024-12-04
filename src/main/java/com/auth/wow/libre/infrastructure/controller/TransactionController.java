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
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PostMapping("/subscription-benefits")
    public ResponseEntity<GenericResponse<Void>> subscriptionBenefits(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid SubscriptionBenefitsDto request) {

        transactionPort.sendSubscriptionBenefits(request.getUserId(), request.getAccountId(),
                request.getCharacterId(), request.getItems(),
                request.getBenefitType(), request.getAmount(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PostMapping("/claim-promotions")
    public ResponseEntity<GenericResponse<Void>> claimPromotions(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid PromotionsDto request) {

        transactionPort.sendPromotion(request.getUserId(), request.getAccountId(),
                request.getCharacterId(), request.getItems(),
                request.getType(), request.getAmount(), request.getMinLvl(), request.getMaxLvl(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PostMapping("/claim-guild-benefits")
    public ResponseEntity<GenericResponse<Void>> claimGuildBenefits(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid BenefitsGuildDto request) {

        transactionPort.sendBenefitsGuild(request.getUserId(), request.getAccountId(),
                request.getCharacterId(), request.getItems(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PostMapping("/claim-machine")
    public ResponseEntity<GenericResponse<MachineClaimDto>> claimGuildBenefits(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid MachineDto request) {

        MachineClaimDto claimSuccess = transactionPort.sendMachine(request.getAccountId(),
                request.getCharacterId(), request.getType(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(claimSuccess, transactionId).ok().build());
    }


}
