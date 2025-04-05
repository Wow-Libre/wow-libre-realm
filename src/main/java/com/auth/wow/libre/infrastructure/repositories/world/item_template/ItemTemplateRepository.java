package com.auth.wow.libre.infrastructure.repositories.world.item_template;

import com.auth.wow.libre.infrastructure.entities.world.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ItemTemplateRepository extends CrudRepository<ItemTemplateEntity, Long> {
    Optional<ItemTemplateEntity> findByEntry(Long entry);

    @Query("SELECT i FROM ItemTemplateEntity i WHERE i.ItemLevel > 100 ORDER BY FUNCTION('RAND')")
    List<ItemTemplateEntity> findRandomItem();
}
