package com.auth.wow.libre.domain.ports.in.transaction;

import com.auth.wow.libre.domain.model.dto.*;

import java.util.*;

public interface TransactionPort {
    void sendItems(Long userId, Long accountId, List<ItemQuantityDto> items, String reference,
                   Double amount, String emulator, String transactionId);

    void sendSubscriptionBenefits(Long userId, Long accountId, Long characterId,
                                  List<ItemQuantityDto> items, String benefitType, Double amount,
                                  String emulator, String transactionId);

    void sendPromotion(Long userId, Long accountId, Long characterId,
                       List<ItemQuantityDto> items, String type, Double amount, Integer minLvl, Integer maxLvl,
                       Integer level, String emulator, String transactionId);

    void sendBenefitsGuild(Long userId, Long accountId, Long characterId, List<ItemQuantityDto> items,
                           String emulator, String transactionId);

    MachineClaimDto sendMachine(Long accountId, Long characterId, String type, String emulator, String transactionId);

    void deductTokens(Long userId, Long accountId, Long characterId, Long points, String transactionId);
}
