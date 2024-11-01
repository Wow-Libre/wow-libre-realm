package com.auth.wow.libre.application.services.bank;

import com.auth.wow.libre.domain.ports.in.bank.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.domain.ports.out.characters.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class BankService implements BankPort {

    private final ObtainCharacters obtainCharacters;
    private final SaveCharacters saveCharacters;
    private final ObtainAccountPort obtainAccountPort;

    public BankService(ObtainCharacters obtainCharacters, SaveCharacters saveCharacters,
                       ObtainAccountPort obtainAccountPort) {
        this.obtainCharacters = obtainCharacters;
        this.saveCharacters = saveCharacters;
        this.obtainAccountPort = obtainAccountPort;
    }

    @Transactional
    @Override
    public Double collectGold(Long userId, Double moneyToPay,  String transactionId) {

        List<AccountEntity> accounts =
                obtainAccountPort.findByUserId(userId).stream().filter(account -> !account.isOnline()).toList();

        if (accounts.isEmpty()) {
            return moneyToPay;
        }

        final Double[] remainingMoneyToPay = {moneyToPay};

        accounts.forEach(account -> {

                    List<CharactersEntity> characters =
                            obtainCharacters.getCharactersAndAccountId(account.getId(),
                                    transactionId).stream().filter(character -> character.getMoney() > 0L).toList();

                    for (CharactersEntity characterPayment : characters) {
                        if (remainingMoneyToPay[0] <= 0) {
                            break;
                        }

                        Double moneyCharacter = characterPayment.getMoney();

                        double amountToDeduct = Math.min(moneyCharacter, moneyToPay);
                        characterPayment.setMoney(characterPayment.getMoney() - amountToDeduct);
                        remainingMoneyToPay[0] = remainingMoneyToPay[0] - amountToDeduct;
                        saveCharacters.save(characterPayment, transactionId);
                    }
                }
        );

        return remainingMoneyToPay[0];
    }

}
