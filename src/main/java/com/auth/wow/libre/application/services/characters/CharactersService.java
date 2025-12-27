package com.auth.wow.libre.application.services.characters;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.character_inventory.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.out.account.*;
import com.auth.wow.libre.domain.ports.out.characters.*;
import com.auth.wow.libre.domain.ports.out.item_template.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.entities.world.*;
import jakarta.xml.bind.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.ws.client.*;
import org.springframework.ws.soap.client.*;

import java.util.*;

@Service
public class CharactersService implements CharactersPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(CharactersService.class);

    private final ObtainCharacters obtainCharacters;
    private final SaveCharacters saveCharacters;
    private final CharacterInventoryPort characterInventoryPort;
    private final ExecuteCommandsPort executeCommandsPort;
    private final ObtainAccountPort obtainAccountPort;
    private final ObtainItemTemplate obtainItemTemplate;

    public CharactersService(ObtainCharacters obtainCharacters, SaveCharacters saveCharacters,
                             CharacterInventoryPort characterInventoryPort, ExecuteCommandsPort executeCommandsPort,
                             ObtainAccountPort obtainAccountPort, ObtainItemTemplate obtainItemTemplate) {
        this.obtainCharacters = obtainCharacters;
        this.saveCharacters = saveCharacters;
        this.characterInventoryPort = characterInventoryPort;
        this.executeCommandsPort = executeCommandsPort;
        this.obtainAccountPort = obtainAccountPort;
        this.obtainItemTemplate = obtainItemTemplate;
    }

    @Override
    public CharactersDto getCharacters(Long accountId, String transactionId) {

        final List<CharacterModel> characterAccount = obtainCharacters
                .getCharactersAndAccountId(accountId, transactionId).stream().map(this::mapToModel).toList();

        CharactersDto charactersDto = new CharactersDto();
        charactersDto.setCharacters(characterAccount.stream()
                .map(CharacterDetailDto::new)
                .toList());
        charactersDto.setTotalQuantity(characterAccount.size());

        return charactersDto;
    }

    @Override
    public CharactersDto loanApplicationCharacters(Long accountId, int level, int totalTimeSeconds,
                                                   String transactionId) {

        final List<CharacterModel> characterAccount = obtainCharacters
                .findByAccountAndLevel(accountId, level, transactionId)
                .stream().filter(character -> character.getTotalTime() >= totalTimeSeconds).map(this::mapToModel)
                .toList();

        CharactersDto charactersDto = new CharactersDto();
        charactersDto.setCharacters(characterAccount.stream()
                .map(CharacterDetailDto::new)
                .toList());
        charactersDto.setTotalQuantity(characterAccount.size());

        return charactersDto;
    }

    @Override
    public CharacterDetailDto getCharacter(Long characterId, Long accountId, String transactionId) {
        return obtainCharacters.getCharacter(characterId, accountId, transactionId)
                .map(this::mapToModel)
                .map(CharacterDetailDto::new)
                .orElse(null);
    }

    @Override
    public CharacterDetailDto getCharacter(Long characterId, String transactionId) {
        return obtainCharacters.getCharacterId(characterId, transactionId)
                .map(this::mapToModel)
                .map(CharacterDetailDto::new)
                .orElse(null);
    }

    @Override
    public void updateMoney(Long characterId, Long amount, String transactionId) {

        Optional<CharactersEntity> character = obtainCharacters.getCharacterId(characterId, transactionId);

        if (character.isEmpty()) {
            throw new InternalException("Could not update character", transactionId);
        }

        CharactersEntity characterUpdate = character.get();
        characterUpdate.setMoney(Double.valueOf(amount));
        saveCharacters.save(characterUpdate, transactionId);
    }

    @Override
    public List<CharactersEntity> getCharactersAvailableMoney(Long accountId, Double money, String transactionId) {
        return obtainCharacters.getCharactersAvailableMoney(accountId, money, transactionId);
    }

    @Override
    public Long count(String transactionId) {
        return obtainCharacters.count();
    }

    @Override
    public FactionsDto factions(String transactionId) {
        return obtainCharacters.factions(transactionId);
    }

    @Override
    public List<LevelRangeDTO> findUserCountsByLevelRange(String transactionId) {
        return obtainCharacters.findUserCountsByLevelRange(transactionId);
    }

    @Override
    public List<CharactersEntity> getCharactersIsLevelMax(Integer level, String transactionId) {
        return obtainCharacters.findByCharactersByLevel(level, transactionId);
    }

    @Override
    public List<CharacterInventoryModel> inventory(Long characterId, Long accountId, String transactionId) {

        CharacterDetailDto characterDetailDto = getCharacter(characterId, accountId, transactionId);

        if (characterDetailDto == null) {
            throw new InternalException("Character Not Found", transactionId);
        }

        return characterInventoryPort.findByAllInventory(characterDetailDto.id, transactionId);
    }

    @Override
    public void transferInventoryItem(Long characterId, Long accountId, Long friendId, Long itemId, Integer quantity,
                                      String transactionId) {

        Optional<AccountEntity> account = obtainAccountPort.findById(accountId);
        if (account.isEmpty() || account.get().isOnline()) {
            throw new InternalException("Please log out of your game account", transactionId);
        }

        CharacterDetailDto characterDetailDto = getCharacter(characterId, accountId, transactionId);

        if (characterDetailDto == null) {
            throw new InternalException("Character Not Found", transactionId);
        }

        Optional<CharactersEntity> friendItem = obtainCharacters.getCharacterId(friendId, transactionId);

        if (friendItem.isEmpty()) {
            throw new InternalException("The recipient customer for the shipment is not found", transactionId);
        }

        CharactersEntity friend = friendItem.get();

        Optional<CharacterInventoryModel> characterInventoryModel = characterInventoryPort
                .findByGuidAndItem(characterId, itemId, transactionId);

        if (characterInventoryModel.isEmpty()) {
            throw new InternalException("It was not possible to find the item associated with the " +
                    "character", transactionId);
        }

        CharacterInventoryModel characterInventory = characterInventoryModel.get();

        if (quantity > characterInventory.getBag()) {
            throw new InternalException("No", transactionId);
        }

        try {
            executeCommandsPort.execute(CommandsCore.sendItem(friend.getName(), "", "",
                    characterInventory.getItemId(), quantity), transactionId);
        } catch (SoapFaultClientException | JAXBException e) {
            LOGGER.error("[CharactersService] [transferInventoryItem] It was not possible to send the item linked to " +
                            "the recipient. TransactionId [{}] - " +
                            "LocalizedMessage [{}] - Message [{}]",
                    transactionId, e.getLocalizedMessage(), e.getMessage());
            throw new InternalException("It was not possible to send the item linked to the recipient", transactionId);
        } catch (WebServiceIOException e) {
            LOGGER.error("Could not communicate with the emulator. TransactionId [{}] - " +
                            "LocalizedMessage [{}] - Message [{}]",
                    transactionId, e.getLocalizedMessage(), e.getMessage());
            throw new InternalException("Could not communicate with the emulator", transactionId);
        }

        characterInventoryPort.delete(characterId, itemId, transactionId);
    }

    @Override
    public void teleport(TeleportDto teleportDto, String transactionId) {
        final Long accountId = teleportDto.getAccountId();
        final Long characterId = teleportDto.getCharacterId();
        final Long userId = teleportDto.getUserId();

        Optional<AccountEntity> account = obtainAccountPort.findByIdAndUserId(accountId, userId);

        if (account.isEmpty()) {
            throw new InternalException("Account Invalid Or Not Found", transactionId);
        }

        if (account.get().isOnline()) {
            throw new InternalException("Please log out of your game account", transactionId);
        }

        Optional<CharactersEntity> characterModel = obtainCharacters.getCharacter(characterId, account.get().getId(),
                transactionId);

        if (characterModel.isEmpty()) {
            throw new InternalException("Character Not Found", transactionId);
        }

        CharactersEntity character = characterModel.get();
        character.setPositionX(teleportDto.getPositionX());
        character.setPositionY(teleportDto.getPositionY());
        character.setPositionZ(teleportDto.getPositionZ());
        character.setMap(teleportDto.getMap());
        character.setOrientation(teleportDto.getOrientation());
        character.setMap(teleportDto.getMap());
        character.setZone(teleportDto.getZone());
        saveCharacters.save(character, transactionId);
    }

    @Override
    public boolean updateStatsCharacter(UpdateStatsRequest request, String transactionId) {
        final Long userId = request.getUserId();
        final Long accountId = request.getAccountId();
        final Long characterId = request.getCharacterId();

        Optional<AccountEntity> account = obtainAccountPort.findByIdAndUserId(accountId, userId);

        if (account.isEmpty()) {
            throw new InternalException("Account Invalid Or Not Found", transactionId);
        }

        if (account.get().isOnline()) {
            throw new InternalException("Please log out of your game account", transactionId);
        }

        Optional<CharactersEntity> charactersEntity = obtainCharacters.getCharacter(characterId, accountId,
                transactionId);

        if (charactersEntity.isEmpty()) {
            throw new InternalException("Character Not Found", transactionId);
        }

        CharactersEntity character = charactersEntity.get();

        Consumables consumables = Consumables.findByName(request.getType());

        switch (consumables) {
            case DREAM:
                updateDream(request.getReference(), character);
                break;
            case THIRST:
                updateThirst(request.getReference(), character);
                break;
            case HUNGER:
                updateHunger(request.getReference(), character);
                break;
            default:
                throw new InternalException("Consumable Type Not Found", transactionId);
        }

        saveCharacters.save(character, transactionId);
        return sendFeedingReward(character, consumables, transactionId);
    }


    private boolean sendFeedingReward(CharactersEntity character, Consumables consumable, String transactionId) {
        Random random = new Random();
        String consumableName = getConsumableName(consumable);
        String characterName = character.getName();

        int wellBeingScore = calculateWellBeingScore(character);

        RewardInfo reward;

        // Sistema de premios basado en probabilidad y bienestar (más justo para todos)
        // 5% de probabilidad de premio épico
        // 15% de probabilidad de premio raro mejorado
        // 30% de probabilidad de premio raro
        // 50% de probabilidad de premio normal
        double randomValue = random.nextDouble() * 100;

        if (randomValue < 5.0) {
            // Premio Épico (5% probabilidad) - burning-blossom
            reward = new RewardInfo(
                    23247L,
                    3,
                    "¡Premio Épico por Alimentarte!",
                    String.format("¡FELICIDADES %s! ¡Has obtenido un PREMIO ÉPICO! " +
                                    "Tu dedicación a mantenerte saludable ha sido recompensada con un premio especial" +
                                    ". " +
                                    "¡Sigue cuidando tu %s para obtener más sorpresas!",
                            characterName.toUpperCase(), consumableName)
            );
        } else if (randomValue < 20.0) {
            // Premio Raro Mejorado (15% probabilidad) - EMBLEMA DE ESCARCHA
            reward = new RewardInfo(
                    49426L,
                    1,
                    "¡Premio Raro Mejorado!",
                    String.format("¡Excelente %s! Has recibido un premio raro mejorado por mantenerte bien alimentado" +
                                    ". " +
                                    "¡Tu bienestar actual es de %d puntos! Sigue cuidándote para obtener premios " +
                                    "épicos.",
                            characterName, wellBeingScore)
            );
        } else if (randomValue < 50.0) {
            // Premio Raro (30% probabilidad) - INSIGNIA DE LA JUSTICIA
            reward = new RewardInfo(
                    29434L,
                    2,
                    "¡Premio Raro!",
                    String.format("¡Bien hecho %s! Por cuidar tu %s, recibes un premio raro. " +
                                    "¡Sigue alimentándote regularmente para obtener mejores recompensas!",
                            characterName, consumableName)
            );
        } else {
            // Premio Normal (50% probabilidad) - EMBLEMA DE TRIUNFO
            reward = new RewardInfo(
                    47241L,
                    2,
                    "¡Premio por Alimentarte!",
                    String.format("¡Bien %s! Por mantenerte bien alimentado, recibes un premio. " +
                                    "¡Sigue cuidando tu %s para obtener premios raros y épicos!",
                            characterName, consumableName)
            );
        }

        try {
            executeCommandsPort.execute(CommandsCore.sendItem(character.getName(), reward.subject(),
                    reward.message(), reward.itemId(), reward.quantity()), transactionId);
            LOGGER.info("[CharactersService] [sendFeedingReward] Reward sent to character {} - " +
                            "Item: {} x{} - Type: {} - WellBeing: {}", character.getName(), reward.itemId(),
                    reward.quantity(), consumable, wellBeingScore);
            Optional<ItemTemplateEntity> item = obtainItemTemplate.findRandomEntry();
            if (item.isPresent() && random.nextDouble() < 0.3) {
                executeCommandsPort.execute(CommandsCore.sendItem(character.getName(), reward.subject(),
                        reward.message(), reward.itemId(), 1), transactionId);
            }
            return true;
        } catch (JAXBException e) {
            LOGGER.error("[CharactersService] [sendFeedingReward] Failed to send reward to character {} - " +
                    "TransactionId [{}] - Error: {}", character.getName(), transactionId, e.getMessage(), e);
            return false;
        }
    }


    private int calculateWellBeingScore(CharactersEntity character) {
        int hunger = character.getHunger() != null ? character.getHunger() : 0;
        int thirst = character.getThirst() != null ? character.getThirst() : 0;
        int dream = character.getDream() != null ? character.getDream() : 0;

        return hunger + thirst + dream;
    }

    /**
     * Record para almacenar la información del premio
     */
    private record RewardInfo(Long itemId, Integer quantity, String subject, String message) {
    }

    /**
     * Obtiene el nombre amigable del consumible para los mensajes
     */
    private String getConsumableName(Consumables consumable) {
        return switch (consumable) {
            case HUNGER -> "hambre";
            case THIRST -> "sed";
            case DREAM -> "sueño";
        };
    }

    private void updateThirst(String reference, CharactersEntity character) {
        StoreItems storeItem = StoreItems.findByCode(reference);


        if (storeItem == null) {
            throw new InternalException("Hunger Consumable Not Found", reference);
        }

        Double money = character.getMoney();

        if (money < storeItem.getCostGold().doubleValue() * 10000) {
            throw new InternalException("Insufficient funds to use the hunger service", reference);
        }

        if (character.getLevel() < 79) {
            character.setXp(character.getXp() + 500);
        }

        Double cost = storeItem.getCostGold().doubleValue() * 10000;
        character.setMoney(money - cost);
        character.setThirst(Math.min(Optional.ofNullable(character.getThirst()).orElse(0) + storeItem.getMultiplier()
                , 100));
    }

    private void updateHunger(String reference, CharactersEntity character) {
        StoreItems storeItem = StoreItems.findByCode(reference);


        if (storeItem == null) {
            throw new InternalException("Hunger Consumable Not Found", reference);
        }

        Double money = character.getMoney();

        if (money < storeItem.getCostGold().doubleValue() * 10000) {
            throw new InternalException("Insufficient funds to use the hunger service", reference);
        }

        if (storeItem.equals(StoreItems.BEER)) {
            character.setDrunk(Math.min(character.getDrunk() + storeItem.getMultiplier(), 100));
        }

        if (character.getLevel() < 79) {
            character.setXp(character.getXp() + 500);
        }

        Double cost = storeItem.getCostGold().doubleValue() * 10000;
        character.setMoney(money - cost);
        character.setHunger(Math.min(Optional.ofNullable(character.getHunger()).orElse(0) + storeItem.getMultiplier()
                , 100));
    }

    private void updateDream(String reference, CharactersEntity character) {
        boolean isHorde = WowRace.getById(character.getRace()).isHorde();
        Double money = character.getMoney();

        if (money < 100D * 10000) {
            throw new InternalException("Insufficient funds to use the hotel service", reference);
        }

        if (StoreItems.HOTEL.getCode().equals(reference)) {
            // Filtrar hoteles según la facción (horda o alianza)
            List<HotelLocations> availableHotels = Arrays.stream(HotelLocations.values())
                    .filter(hotel -> hotel.isHorde() == isHorde)
                    .toList();

            if (availableHotels.isEmpty()) {
                throw new InternalException("No hotel locations found for faction", reference);
            }

            // Seleccionar un hotel aleatorio
            Random random = new Random();
            HotelLocations selectedHotel = availableHotels.get(random.nextInt(availableHotels.size()));
            if (character.getLevel() < 79) {
                character.setXp(character.getXp() + 500);
            }

            character.setPositionX(selectedHotel.getX());
            character.setPositionY(selectedHotel.getY());
            character.setPositionZ(selectedHotel.getZ());
            character.setMap(selectedHotel.getArea());
            character.setOrientation(selectedHotel.getOrientation());
            character.setZone((selectedHotel.getZona().intValue()));
            character.setDream(StoreItems.HOTEL.getMultiplier());
            Double cost = 100D * 10000;
            character.setMoney(money - cost);
        } else {
            throw new InternalException("Dream Consumable Not Found", reference);
        }
    }

    private CharacterModel mapToModel(CharactersEntity characters) {
        long gold = characters.getMoney().longValue() / 10000;
        long copper = characters.getMoney().longValue() % 10000;
        WowRace characterRace = WowRace.getById(characters.getRace());
        int gender = characters.getGender();
        WowClass classCharacter = WowClass.getById(characters.getClassCharacters());

        return CharacterModel.builder()
                .raceName(characterRace.getRaceName())
                .raceLogo(gender == 1 ? characterRace.getFemaleIcon() : characterRace.getMaleIcon())
                .guid(characters.getGuid()).account(characters.getAccount())
                .name(characters.getName()).raceId(characters.getRace())
                .classCharacters(characters.getClassCharacters())
                .classLogo(classCharacter.getLogo())
                .className(classCharacter.getClassName())
                .gender(gender)
                .level(characters.getLevel())
                .xp(characters.getXp())
                .money(characters.getMoney())
                .bankSlots(characters.getBankSlots())
                .positionX(characters.getPositionX())
                .positionY(characters.getPositionY())
                .positionZ(characters.getPositionZ())
                .map(characters.getMap())
                .instanceId(characters.getInstanceId())
                .orientation(characters.getOrientation())
                .taximask(characters.getTaximask())
                .online(characters.getOnline())
                .cinematic(characters.getCinematic())
                .totalTime(characters.getTotalTime())
                .levelTime(characters.getLevelTime())
                .logoutTime(characters.getLogoutTime())
                .isLogoutResting(characters.getIsLogoutResting())
                .restBonus(characters.getRestBonus())
                .resetTalentsCost(characters.getResetTalentsCost())
                .resetTalentsTime(characters.getResetTalentsTime()).transX(characters.getTransX())
                .transY(characters.getTransY()).transZ(characters.getTransZ()).transO(characters.getTransO())
                .atLogin(characters.getAtLogin())
                .deathExpireTime(characters.getDeathExpireTime())
                .taxiPath(0)
                .yesterdayKills(characters.getYesterdayKills())
                .totalKills(characters.getTotalKills())
                .todayKills(characters.getTodayKills())
                .chosenTitle(characters.getChosenTitle())
                .watchedFaction(characters.getWatchedFaction())
                .drunk(characters.getDrunk())
                .health(characters.getHealth()).power1(characters.getPower1()).power2(characters.getPower2())
                .power3(characters.getPower3())
                .power4(characters.getPower4())
                .power5(characters.getPower5())
                .power6(characters.getPower6())
                .power7(characters.getPower7())
                .activeTalentGroup(characters.getActiveTalentGroup())
                .exploredZones(characters.getExploredZones())
                .equipmentCache(characters.getEquipmentCache())
                .knownTitles(characters.getKnownTitles())
                .actionsBars(characters.getActionBars())
                .gold(gold)
                .copper(copper % 100)
                .silver(gold / 100)
                .hunger(characters.getHunger())
                .dream(characters.getDream())
                .thirst(characters.getThirst())
                .build();
    }

}
