package com.auth.wow.libre.domain.ports.in.transaction;

import com.auth.wow.libre.domain.model.dto.*;

import java.util.*;

public interface TransactionPort {
    void sendItems(Long userId, Long accountId, List<ItemQuantityDto> items, String reference,
                   Double amount, String transactionId);

}
