package com.auth.wow.libre.infrastructure.repositories.characters.mail;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface MailRepository extends CrudRepository<MailEntity, Long> {
    List<MailEntity> findByReceiverGuidId(Long characterId);

    @Query("SELECT new com.auth.wow.libre.infrastructure.repositories.characters.mail.MailEntityModel(" +
            "m.mailId, m.characterReceiverId, " +
            " ii.itemEntry, ii.duration, ii.id) " +
            "FROM MailItemsEntity m " +
            "JOIN ItemInstanceEntity ii ON ii.id = m.itemId " +
            "WHERE m.mailId = :mailId")
    List<MailEntityModel> findByMailsAndItems(@Param("mailId") Long mailId);
}
