package com.auth.wow.libre.application.services.transaction;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.character_service.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.in.transaction.*;
import com.auth.wow.libre.domain.ports.out.item_template.*;
import com.auth.wow.libre.infrastructure.entities.world.*;
import jakarta.xml.bind.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.time.*;
import java.util.*;

import static com.auth.wow.libre.domain.model.constant.Constants.Errors.*;

@Service
public class TransactionService implements TransactionPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    public static final String URL_GOLD_IMG_WIX = "https://static.wixstatic" +
            ".com/media/5dd8a0_effdc521b002493682c947c5e2aa283d~mv2.webp";
    public static final String URL_LEVEL_WIX = "https://static.wixstatic" +
            ".com/media/5dd8a0_67020ed5ab7c46e4be8085fb20563eb7~mv2.webp";
    public static final String IMG_URL_UPGRADE_LEVEL_SLOT = "https://static.wixstatic" +
            ".com/media/5dd8a0_d081e7ab00364860b9dceca9f5711e85~mv2.webp";

    private final CharactersPort charactersPort;
    private final CharacterTransactionPort characterTransactionPort;
    private final ExecuteCommandsPort executeCommandsPort;
    private final ObtainItemTemplate obtainItemTemplate;
    private final AccountPort accountPort;

    public TransactionService(CharactersPort charactersPort, CharacterTransactionPort characterTransactionPort,
                              ExecuteCommandsPort executeCommandsPort, ObtainItemTemplate obtainItemTemplate,
                              AccountPort accountPort) {
        this.charactersPort = charactersPort;
        this.characterTransactionPort = characterTransactionPort;
        this.executeCommandsPort = executeCommandsPort;
        this.obtainItemTemplate = obtainItemTemplate;
        this.accountPort = accountPort;
    }

    @Transactional
    @Override
    public void sendItems(Long userId, Long accountId, List<ItemQuantityDto> items, String reference,
                          Double amount, String transactionId) {

        LOGGER.info("[TransactionService] [sendItems] accountId {} userId {} items {} reference {} amount {}",
                accountId, userId, items, reference, amount);
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
                    String command = CommandsCore.sendItems(character.name, "", "Thank you for your kind " +
                            "donation! Your support truly means a lot.", items);

                    characterTransactionPort.create(CharacterTransactionModel.builder()
                                    .command(command)
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
                    try {
                        executeCommandsPort.execute(command, transactionId);
                    } catch (JAXBException ignored) {
                    }


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
                              Double amount, Integer minLvl, Integer maxLvl, Integer level, String transactionId) {

        CharacterDetailDto characterDetailDto = charactersPort.getCharacter(characterId, accountId, transactionId);

        if (characterDetailDto.getLevel() < minLvl || characterDetailDto.getLevel() > maxLvl) {
            throw new InternalException("You do not meet the required level", transactionId);
        }

        String characterName = characterDetailDto.name;
        PromotionType benefitTypeEnum = PromotionType.findByName(type);

        final String command = switch (benefitTypeEnum) {
            case ITEM -> CommandsCore.sendItems(characterName, "", "", items);
            case LEVEL -> CommandsCore.sendLevel(characterName, level);
            case MONEY ->
                    CommandsCore.sendMoney(characterName, "", "", amount != null ? String.valueOf(amount.intValue())
                            : "0");
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
            LOGGER.error("sendBenefitsGuild It was not possible to claim the premium benefit, something has failed in" +
                    "the execution of the core azeroth/trinity {}", transactionId);
            throw new InternalException("It was not possible to claim the premium benefit, something has failed in " +
                    "the execution of the core azeroth/trinity", transactionId);
        }
    }

    @Override
    public MachineClaimDto sendMachine(Long accountId, Long characterId, String type, String transactionId) {
        MachineType machineType = MachineType.findByName(type);

        CharactersDto characterDetailDto = charactersPort.getCharacters(accountId, transactionId);

        if (characterDetailDto == null || characterDetailDto.getCharacters() == null) {
            throw new InternalException("Could not get character data", transactionId);
        }

        List<CharacterDetailDto> characters = characterDetailDto.getCharacters();

        Random random = new Random();
        String command;

        ItemQuantityDto itemQuantityDto = new ItemQuantityDto();
        String name = null;
        String logo = null;

        switch (machineType) {
            case ITEMS:
                Optional<ItemTemplateEntity> item = obtainItemTemplate.findRandomEntry();

                if (item.isEmpty()) {
                    ItemsMachineType itemsRandom =
                            ItemsMachineType.values()[random.nextInt(ItemsMachineType.values().length)];
                    itemQuantityDto.setId(itemsRandom.getCode());
                    itemQuantityDto.setQuantity(1);
                    name = itemsRandom.getName();
                    logo = itemsRandom.getLogo();
                    command = CommandsCore.sendItems(null, "", "", List.of(itemQuantityDto));
                } else {
                    ItemTemplateEntity itemTemplateEntity = item.get();
                    itemQuantityDto.setId(itemTemplateEntity.getEntry().toString());
                    itemQuantityDto.setQuantity(1);
                    name = itemTemplateEntity.getEntry().toString();
                    logo = "https://static.wixstatic.com/media/5dd8a0_ecda67ad536d4e40ab5ed0d768b105d3~mv2.png";
                    command = CommandsCore.sendItems(null, "", "", List.of(itemQuantityDto));
                }
                break;
            case LEVEL:
                int levelMax = 80;
                name = String.format("Level: %s", levelMax);
                logo = IMG_URL_UPGRADE_LEVEL_SLOT;
                // Asigna el comando para LEVEL
                command = CommandsCore.sendLevel(null, levelMax);
                break;
            case MOUNT:
                MountType mountType = MountType.values()[random.nextInt(MountType.values().length)];
                name = mountType.getCode();
                logo = mountType.getLogo();

                itemQuantityDto.setId(mountType.getCode());
                itemQuantityDto.setQuantity(1);
                // Asigna el comando para MOUNT
                command = CommandsCore.sendItems(null, "", "", List.of(itemQuantityDto));
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
                // Asigna el comando para GOLD
                command = CommandsCore.sendMoney(null, "", "", String.valueOf(randomCopper));
                break;
            default:
                return new MachineClaimDto(false);
        }

        for (CharacterDetailDto character : characters) {
            try {
                // Reemplaza el nombre del personaje en el comando generado
                String personalizedCommand = command.replaceFirst("null", character.name);
                executeCommandsPort.execute(personalizedCommand, transactionId);
            } catch (Exception e) {
                LOGGER.error("It was not possible to send the coin slot winner's prize {}", transactionId);
                return new MachineClaimDto(false);
            }
        }

        return new MachineClaimDto(logo, name, true);
    }

    @Override
    public void deductTokens(Long userId, Long accountId, Long characterId, Long points, String transactionId) {
        LOGGER.info("[TransactionService] [deductTokens] accountId {} userId {} characterId {}",
                accountId, userId, characterId);

        if (accountPort.isOnline(accountId, transactionId)) {
            LOGGER.error("[TransactionService] [deductTokens] Cannot deduct tokens while character is online");
            throw new InternalException("Cannot deduct tokens while character is online", transactionId);
        }

        CharacterDetailDto character = charactersPort.getCharacter(characterId, accountId, transactionId);

        if (character == null) {
            LOGGER.error("[TransactionService] [deductTokens] Could not get character data");
            throw new InternalException(CONSTANT_ERROR_NOT_POSSIBLE_OBTAIN_CHARACTER, transactionId);
        }

        if (character.getMoney() < TransactionType.CHANGE_COINS.getCost() * points) {
            LOGGER.error("[TransactionService] [deductTokens] You don't have enough money to send the money");
            throw new InternalException("You don't have enough money to send the money", transactionId);
        }

        charactersPort.updateMoney(character.id,
                (long) (character.getMoney() - TransactionType.CHANGE_COINS.getCost() * points),
                transactionId);
    }


}