package com.auth.wow.libre.domain.model.dto;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class AccountsDto {
    private List<AccountsServerDto> accounts;
    private Long size;
}
