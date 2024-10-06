package com.auth.wow.libre.domain.model.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
public class CharactersDto {
    private List<CharacterDetailDto> characters;
    @JsonProperty("total_quantity")
    private Integer totalQuantity;
}
