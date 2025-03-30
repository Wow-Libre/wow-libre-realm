package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.character_social.*;
import com.auth.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharactersSocialControllerTest {

    @Mock
    private CharacterSocialPort characterSocialPort;

    @InjectMocks
    private CharactersSocialController charactersSocialController;

    private final String transactionId = "12345";
    private final Long characterId = 1L;
    private final Long accountId = 10L;
    private final Long userId = 100L;
    private final Long friendId = 200L;

    @Test
    void testDeleteFriend_Success() {
        ResponseEntity<GenericResponse<Void>> response =
                charactersSocialController.deleteFriend(transactionId, friendId, characterId, accountId, userId);

        verify(characterSocialPort, times(1)).deleteFriend(characterId, accountId, friendId, userId, transactionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetFriends_Success() {
        CharacterSocialDto characterSocialDto = new CharacterSocialDto();
        when(characterSocialPort.getFriends(characterId, transactionId)).thenReturn(characterSocialDto);

        ResponseEntity<GenericResponse<CharacterSocialDto>> response =
                charactersSocialController.friends(transactionId, characterId);

        verify(characterSocialPort, times(1)).getFriends(characterId, transactionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetFriends_NoContent() {
        when(characterSocialPort.getFriends(characterId, transactionId)).thenReturn(null);

        ResponseEntity<GenericResponse<CharacterSocialDto>> response =
                charactersSocialController.friends(transactionId, characterId);

        verify(characterSocialPort, times(1)).getFriends(characterId, transactionId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testSendMoney_Success() {
        SendMoneyDto sendMoneyDto = new SendMoneyDto();
        sendMoneyDto.setMoney(500L);
        sendMoneyDto.setCharacterId(characterId);
        sendMoneyDto.setAccountId(accountId);
        sendMoneyDto.setUserId(userId);
        sendMoneyDto.setFriendId(friendId);
        sendMoneyDto.setMoney(500L);
        sendMoneyDto.setFriendId(friendId);
        ResponseEntity<GenericResponse<CharactersDto>> response =
                charactersSocialController.sendMoney(transactionId, sendMoneyDto);

        verify(characterSocialPort, times(1)).sendMoney(
                characterId, accountId, userId, friendId, sendMoneyDto.getMoney(), sendMoneyDto.getCost(),
                transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSendLevel_Success() {
        SendLevelDto sendLevelDto = new SendLevelDto();
        sendLevelDto.setLevel(5);
        sendLevelDto.setCost(100D);
        sendLevelDto.setCharacterId(characterId);
        sendLevelDto.setAccountId(accountId);
        sendLevelDto.setUserId(userId);
        sendLevelDto.setFriendId(friendId);

        ResponseEntity<GenericResponse<CharactersDto>> response =
                charactersSocialController.sendLevel(transactionId, sendLevelDto);

        verify(characterSocialPort, times(1)).sendLevel(
                characterId, accountId, userId, friendId, sendLevelDto.getLevel(), sendLevelDto.getCost(),
                transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
