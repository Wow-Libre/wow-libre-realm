package com.auth.wow.libre.infrastructure.entities.world;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "item_template")
public class ItemTemplateEntity {
    @Id
    private Long entry;
    private String name;
}
