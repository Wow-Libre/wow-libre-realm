package com.auth.wow.libre.domain.model.dto;

import com.auth.wow.libre.domain.model.*;
import lombok.*;

import java.util.*;

@Data
public class GuildsDto {
    private List<GuildModel> guilds;
    private Long size;
}
