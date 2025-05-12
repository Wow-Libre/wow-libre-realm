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
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
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

    public CharactersService(ObtainCharacters obtainCharacters, SaveCharacters saveCharacters,
                             CharacterInventoryPort characterInventoryPort, ExecuteCommandsPort executeCommandsPort,
                             ObtainAccountPort obtainAccountPort) {
        this.obtainCharacters = obtainCharacters;
        this.saveCharacters = saveCharacters;
        this.characterInventoryPort = characterInventoryPort;
        this.executeCommandsPort = executeCommandsPort;
        this.obtainAccountPort = obtainAccountPort;
    }


    @Override
    public CharactersDto getCharacters(Long accountId, String transactionId) {

        final List<CharacterModel> characterAccount =
                obtainCharacters.getCharactersAndAccountId(accountId, transactionId).stream().map(this::mapToModel).toList();

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

        final List<CharacterModel> characterAccount =
                obtainCharacters.findByAccountAndLevel(accountId, level, transactionId)
                        .stream().filter(character -> character.getTotalTime() >= totalTimeSeconds).map(this::mapToModel).toList();

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


        Optional<CharacterInventoryModel> characterInventoryModel =
                characterInventoryPort.findByGuidAndItem(characterId, itemId, transactionId);

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
                .knownCurrencies(characters.getKnownCurrencies())
                .watchedFaction(characters.getWatchedFaction())
                .drunk(characters.getDrunk())
                .health(characters.getHealth()).power1(characters.getPower1()).power2(characters.getPower2())
                .power3(characters.getPower3())
                .power4(characters.getPower4())
                .power5(characters.getPower5())
                .power6(characters.getPower6())
                .power7(characters.getPower7())
                .talentGroupsCount(characters.getTalentGroupsCount())
                .activeTalentGroup(characters.getActiveTalentGroup())
                .exploredZones(characters.getExploredZones())
                .equipmentCache(characters.getEquipmentCache())
                .ammold(characters.getAmmoId())
                .knownTitles(characters.getKnownTitles())
                .actionsBars(characters.getActionBars())
                .grantableLevels(characters.getGrantableLevels())
                .gold(gold)
                .copper(copper % 100)
                .silver(gold / 100)
                .build();
    }

}
