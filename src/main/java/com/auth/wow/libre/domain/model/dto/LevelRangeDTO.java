package com.auth.wow.libre.domain.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelRangeDTO {
    private String levelRange;
    private Long userCount;
}
