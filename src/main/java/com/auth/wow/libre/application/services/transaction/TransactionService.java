package com.auth.wow.libre.application.services.transaction;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.character_service.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.transaction.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.time.*;
import java.util.*;

@Service
public class TransactionService implements TransactionPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private final CharactersPort charactersPort;
    private final CharacterTransactionPort characterTransactionPort;


    public TransactionService(CharactersPort charactersPort, CharacterTransactionPort characterTransactionPort) {
        this.charactersPort = charactersPort;
        this.characterTransactionPort = characterTransactionPort;
    }

    @Transactional
    @Override
    public void sendItems(Long userId, Long accountId, List<ItemQuantityDto> items, String reference,
                          Double amount, String transactionId) {


        CharactersDto characters = charactersPort.getCharacters(accountId, transactionId);

        if (characters == null || characters.getCharacters().isEmpty()) {
            throw new InternalException("No cuenta con personajes para enviar items", transactionId);
        }

        characters.getCharacters().forEach(character -> {

                    String referenceId = character.id + reference;

                    if (characterTransactionPort.existTransaction(referenceId, transactionId)) {
                        LOGGER.error("[TransactionService] [sendItems] The reference already exists, so at some point" +
                                        " the item " +
                                        "must have been sent. Reference {}",
                                reference);
                        return;
                    }

                    characterTransactionPort.create(CharacterTransactionModel.builder()
                                    .command(CommandsCore.sendItems(character.name, "", "Thank you for your kind " +
                                            "donation! Your support truly means a lot.", items))
                                    .accountId(accountId)
                                    .userId(userId)
                                    .reference(referenceId)
                                    .amount(amount)
                                    .indebtedness(amount != null && amount > 0)
                                    .transactionType(TransactionType.SEND_ITEMS)
                                    .successful(true)
                                    .status(true)
                                    .characterId(character.id).transactionDate(LocalDateTime.now()).build(),
                            transactionId);

                }
        );

    }
}
