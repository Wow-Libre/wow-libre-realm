package com.auth.wow.libre.domain.model.dto.view;

import lombok.*;

@Data
@AllArgsConstructor
public class Card {
    private Long id;
    private String icon;
    private String title;
    private String description;
}
