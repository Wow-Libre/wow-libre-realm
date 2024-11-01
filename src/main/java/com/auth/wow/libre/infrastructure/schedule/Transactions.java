package com.auth.wow.libre.infrastructure.schedule;

import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.ports.in.character_service.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import jakarta.xml.bind.*;
import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Component
public class Transactions {
    private static final Logger LOGGER = LoggerFactory.getLogger(Transactions.class);

    private final CharacterTransactionPort characterTransactionPort;
    private final ExecuteCommandsPort executeCommandsPort;

    public Transactions(CharacterTransactionPort characterTransactionPort, ExecuteCommandsPort executeCommandsPort) {
        this.characterTransactionPort = characterTransactionPort;
        this.executeCommandsPort = executeCommandsPort;
    }

    @Scheduled(cron = " */10 * * * * *")
    @Transactional
    public void sendCreditLoans() {
        String transactionId = "";
        List<CharacterTransactionEntity> characterTransaction =
                characterTransactionPort.findByTransactionType(TransactionType.SEND_ITEMS, "");

        if (characterTransaction.isEmpty()) {
            return;
        }

        characterTransaction.forEach(transaction -> {
            try {
                executeCommandsPort.execute(transaction.getCommand(), transactionId);
                transaction.setStatus(false);
                characterTransactionPort.update(transaction, transactionId);
            } catch (JAXBException e) {
                LOGGER.error("Send invalid items");
            }
        });

    }

}
