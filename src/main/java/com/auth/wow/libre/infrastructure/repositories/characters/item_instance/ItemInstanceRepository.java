package com.auth.wow.libre.infrastructure.repositories.characters.item_instance;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ItemInstanceRepository extends CrudRepository<ItemInstanceEntity, Long> {

    Optional<ItemInstanceEntity> findById(Long id);

    Optional<ItemInstanceEntity> findByIdAndOwnerGuid(Long id, Long ownerGuid);
}
