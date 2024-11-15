package com.auth.wow.libre.domain.model.dto;

import lombok.*;

@Data
public class CreateClientDto {
    private String username;
    private String password;
    private byte[] salt;
}
