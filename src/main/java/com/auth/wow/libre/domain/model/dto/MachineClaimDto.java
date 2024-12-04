package com.auth.wow.libre.domain.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class MachineClaimDto {
    private String logo;
    private String name;
    private boolean send;


    public MachineClaimDto(boolean send) {
        this.send = send;
    }
}
