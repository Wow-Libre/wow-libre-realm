package com.auth.wow.libre.application.services.wow_libre;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.wow_libre.*;
import com.auth.wow.libre.infrastructure.client.*;
import org.springframework.stereotype.*;

@Service
public class WowLibreService implements WowLibrePort {
    private final WowLibreClient wowLibreClient;

    public WowLibreService(WowLibreClient wowLibreClient) {
        this.wowLibreClient = wowLibreClient;
    }

    @Override
    public String getJwt(String transactionId) {
        LoginResponseDto jwtDto = wowLibreClient.login(transactionId);

        if (jwtDto == null) {
            throw new InternalException("It was not possible to authenticate with the free wow central, please " +
                    "contact support", transactionId);
        }


        return jwtDto.jwt;
    }

    @Override
    public ServerModel apiSecret(String jwt, String transactionId) {
        ServerDto serverDto = wowLibreClient.secret(jwt, transactionId);

        if (serverDto == null) {
            throw new InternalException("No fue posible autenticarte con la central de wow libre por favor comunicate" +
                    " con el soporte", transactionId);
        }

        return new ServerModel(serverDto.getApiSecret());
    }
}
