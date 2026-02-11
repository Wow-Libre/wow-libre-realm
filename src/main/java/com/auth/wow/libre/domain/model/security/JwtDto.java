package com.auth.wow.libre.domain.model.security;

import com.fasterxml.jackson.annotation.*;

import java.util.*;


public record JwtDto(String jwt, @JsonProperty("refresh_token") String refreshToken,
                     @JsonProperty("expiration_date") Date expirationDate) {
}
