package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.*;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
public class CharacterSocialDto {
    private List<CharacterSocialDetail> friends;
    @JsonProperty("total_quantity")
    private Integer totalQuantity;
}
