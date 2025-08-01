package com.auth.wow.libre.domain.ports.in.wow_libre;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;

public interface WowLibrePort {
    ServerKey getApiSecret(String apikey, String transactionId);
}
