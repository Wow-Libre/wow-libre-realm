package com.auth.wow.libre.application.services.transaction;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.character_service.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.in.transaction.*;
import jakarta.xml.bind.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.time.*;
import java.util.*;

@Service
public class TransactionService implements TransactionPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    public static final String URL_GOLD_IMG_WIX = "https://static.wixstatic" +
            ".com/media/5dd8a0_effdc521b002493682c947c5e2aa283d~mv2.webp";
    public static final String URL_LEVEL_WIX = "https://static.wixstatic" +
            ".com/media/5dd8a0_67020ed5ab7c46e4be8085fb20563eb7~mv2.webp";

    private final CharactersPort charactersPort;
    private final CharacterTransactionPort characterTransactionPort;
    private final ExecuteCommandsPort executeCommandsPort;

    public TransactionService(CharactersPort charactersPort, CharacterTransactionPort characterTransactionPort,
                              ExecuteCommandsPort executeCommandsPort) {
        this.charactersPort = charactersPort;
        this.characterTransactionPort = characterTransactionPort;
        this.executeCommandsPort = executeCommandsPort;
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

    @Override
    public void sendSubscriptionBenefits(Long userId, Long accountId, Long characterId,
                                         List<ItemQuantityDto> items, String benefitType, Double amount,
                                         String transactionId) {

        CharacterDetailDto characterDetailDto = charactersPort.getCharacter(characterId, accountId, transactionId);

        String characterName = characterDetailDto.name;
        BenefitType benefitTypeEnum = BenefitType.findByName(benefitType);

        final String command = switch (benefitTypeEnum) {
            case ITEM -> CommandsCore.sendItems(characterName, "", "", items);
            case CUSTOMIZE -> CommandsCore.characterCustomize(characterName);
            case CHANGE_FACTION -> CommandsCore.characterChangeFaction(characterName);
            case CHANGE_RACE -> CommandsCore.characterChangeRace(characterName);
            default -> {
                LOGGER.error("Benefit Type Not found");
                yield CommandsCore.sendMail(characterName, "Ups", "We are sorry, an error occurred when claiming " +
                        "your benefit, please contact support");
            }
        };

        try {
            executeCommandsPort.execute(command, transactionId);
        } catch (JAXBException e) {
            LOGGER.error("It was not possible to claim the premium benefit, something has failed in \" +\n" +
                    "                    \"the execution of the core azeroth/trinity {}", transactionId);
            throw new InternalException("It was not possible to claim the premium benefit, something has failed in " +
                    "the execution of the core azeroth/trinity", transactionId);
        }
    }

    @Override
    public void sendPromotion(Long userId, Long accountId, Long characterId, List<ItemQuantityDto> items, String type,
                              Double amount, Integer minLvl, Integer maxLvl, String transactionId) {

        CharacterDetailDto characterDetailDto = charactersPort.getCharacter(characterId, accountId, transactionId);

        if (characterDetailDto.getLevel() < minLvl || characterDetailDto.getLevel() > maxLvl) {
            throw new InternalException("You do not meet the required level", transactionId);
        }

        String characterName = characterDetailDto.name;
        PromotionType benefitTypeEnum = PromotionType.findByName(type);

        final String command = switch (benefitTypeEnum) {
            case ITEM -> CommandsCore.sendItems(characterName, "", "", items);
            case LEVEL -> CommandsCore.sendLevel(characterName, 80);
            case MONEY -> CommandsCore.sendMoney(characterName, "", "", amount != null ? amount.toString() : "0");
        };

        try {
            executeCommandsPort.execute(command, transactionId);
        } catch (JAXBException e) {
            LOGGER.error("It was not possible to claim the premium benefit, something has failed in \" +\n" +
                    "                    \"the execution of the core azeroth/trinity {}", transactionId);
            throw new InternalException("It was not possible to claim the premium benefit, something has failed in " +
                    "the execution of the core azeroth/trinity", transactionId);
        }
    }

    @Override
    public void sendBenefitsGuild(Long userId, Long accountId, Long characterId, List<ItemQuantityDto> items,
                                  String transactionId) {

        CharacterDetailDto characterDetailDto = charactersPort.getCharacter(characterId, accountId, transactionId);

        if (characterDetailDto == null) {
            throw new InternalException("Could not get character data", transactionId);
        }

        String characterName = characterDetailDto.name;

        try {
            executeCommandsPort.execute(CommandsCore.sendItems(characterName, "", "", items), transactionId);
        } catch (JAXBException e) {
            LOGGER.error("It was not possible to claim the premium benefit, something has failed in \" +\n" +
                    "                    \"the execution of the core azeroth/trinity {}", transactionId);
            throw new InternalException("It was not possible to claim the premium benefit, something has failed in " +
                    "the execution of the core azeroth/trinity", transactionId);
        }
    }

    @Override
    public MachineClaimDto sendMachine(Long accountId, Long characterId, String type, String transactionId) {
        MachineType machineType = MachineType.getName(type);

        CharacterDetailDto characterDetailDto = charactersPort.getCharacter(characterId, accountId, transactionId);

        if (characterDetailDto == null) {
            throw new InternalException("Could not get character data", transactionId);
        }

        Random random = new Random();
        int randomNumber = random.nextInt(10) + 1;
        String command;
        ItemQuantityDto itemQuantityDto = new ItemQuantityDto();
        String name = null;
        String logo = null;

        switch (machineType) {
            case ITEMS:
                ItemsMachineType itemsRandom =
                        ItemsMachineType.values()[random.nextInt(ItemsMachineType.values().length)];
                itemQuantityDto.setId(itemsRandom.getCode());
                itemQuantityDto.setQuantity(itemsRandom.isLimit() ? 1 : randomNumber);
                name = itemsRandom.getName();
                logo = itemsRandom.getLogo();
                command = CommandsCore.sendItems(characterDetailDto.name, "", "", List.of(itemQuantityDto));
                break;
            case LEVEL:
                int levelMax = 80;
                int level = characterDetailDto.level + randomNumber;
                name = String.format("Level: %s", level);
                logo = URL_LEVEL_WIX;
                command = CommandsCore.sendLevel(characterDetailDto.name, Math.min(level, levelMax));
                break;
            case MENAS:
                MineType randomMine = MineType.values()[random.nextInt(MineType.values().length)];
                name = randomMine.getName();
                logo = randomMine.getLogo();

                itemQuantityDto.setId(randomMine.getCode());
                itemQuantityDto.setQuantity(randomNumber);
                command = CommandsCore.sendItems(characterDetailDto.name, "", "", List.of(itemQuantityDto));
                break;
            case GOLD:

                int minGold = 10;
                int maxGold = minGold * 20;
                int minCopper = minGold * 100_000;
                int maxCopper = maxGold * 100_000;

                int randomCopper = random.nextInt(maxCopper - minCopper + 1) + minCopper;
                int gold = randomCopper / 10_000;

                name = String.format("Send %s Gold", gold);
                logo = URL_GOLD_IMG_WIX;
                command = CommandsCore.sendMoney(characterDetailDto.name, "", "", String.valueOf(randomCopper));
                break;
            default:
                return new MachineClaimDto(false);

        }

        try {
            executeCommandsPort.execute(command, transactionId);
            return new MachineClaimDto(logo, name, true);
        } catch (Exception e) {
            LOGGER.error("It was not possible to send the coin slot winner's prize {}", transactionId);
            return new MachineClaimDto(false);
        }
    }
}