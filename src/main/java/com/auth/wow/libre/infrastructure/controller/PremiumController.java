package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.premium.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/premium")
public class PremiumController {

    private final PremiumPort premiumPort;

    public PremiumController(PremiumPort premiumPort) {
        this.premiumPort = premiumPort;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<GenericResponse<Boolean>> isPremium(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable Long accountId) {
        boolean isPremium = premiumPort.isPremium(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponseBuilder<Boolean>(transactionId)
                .ok(isPremium).build());
    }

    @PostMapping("/{accountId}")
    public ResponseEntity<GenericResponse<Void>> createPremium(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable Long accountId, @RequestParam boolean active) {
        premiumPort.savePremiumStatus(accountId, active);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponseBuilder<Void>(transactionId)
                .ok().build());
    }

}
