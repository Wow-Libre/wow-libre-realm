package com.auth.wow.libre.domain.ports.in.transaction;

import com.auth.wow.libre.domain.model.dto.*;

import java.util.*;

public interface TransactionPort {
    void sendItems(Long userId, Long accountId, List<ItemQuantityDto> items, String reference,
                   Double amount, String transactionId);

    void sendSubscriptionBenefits(Long userId, Long accountId, Long characterId,
                                  List<ItemQuantityDto> items, String benefitType, Double amount,
                                  String transactionId);

    void sendPromotion(Long userId, Long accountId, Long characterId,
                       List<ItemQuantityDto> items, String type, Double amount,
                       String transactionId);

    void sendBenefitsGuild(Long userId, Long accountId, Long characterId, List<ItemQuantityDto> items,
                           String transactionId);
}
