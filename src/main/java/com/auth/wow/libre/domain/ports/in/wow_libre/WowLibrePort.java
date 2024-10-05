package com.auth.wow.libre.domain.ports.in.wow_libre;

import com.auth.wow.libre.domain.model.*;

public interface WowLibrePort {
    String getJwt(String transactionId);

    ServerModel apiSecret(String jwt, String transactionId);
}
