package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharactersControllerTest {
    @Mock
    private CharactersPort charactersPort;

    @InjectMocks
    private CharactersController charactersController;

    private static final String TRANSACTION_ID = "12345";
    private static final Long ACCOUNT_ID = 10L;
    private static final Long CHARACTER_ID = 1L;
    private static final Long FRIEND_ID = 20L;
    private static final Long ITEM_ID = 100L;

    @Test
    void testGetCharacters_Success() {
        CharactersDto charactersDto = new CharactersDto();
        when(charactersPort.getCharacters(ACCOUNT_ID, TRANSACTION_ID)).thenReturn(charactersDto);

        ResponseEntity<GenericResponse<CharactersDto>> response =
                charactersController.characters(TRANSACTION_ID, ACCOUNT_ID);

        verify(charactersPort, times(1)).getCharacters(ACCOUNT_ID, TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetCharacters_NoContent() {
        when(charactersPort.getCharacters(ACCOUNT_ID, TRANSACTION_ID)).thenReturn(null);

        ResponseEntity<GenericResponse<CharactersDto>> response =
                charactersController.characters(TRANSACTION_ID, ACCOUNT_ID);

        verify(charactersPort, times(1)).getCharacters(ACCOUNT_ID, TRANSACTION_ID);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testLoanApplicationCharacters_Success() {
        CharactersDto charactersDto = new CharactersDto();
        when(charactersPort.loanApplicationCharacters(ACCOUNT_ID, 10, 30, TRANSACTION_ID)).thenReturn(charactersDto);

        ResponseEntity<GenericResponse<CharactersDto>> response =
                charactersController.loanApplicationCharacters(TRANSACTION_ID, ACCOUNT_ID, 30, 10);

        verify(charactersPort, times(1)).loanApplicationCharacters(ACCOUNT_ID, 10, 30, TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testLoanApplicationCharacters_NoContent() {
        when(charactersPort.loanApplicationCharacters(ACCOUNT_ID, 10, 30, TRANSACTION_ID)).thenReturn(null);

        ResponseEntity<GenericResponse<CharactersDto>> response =
                charactersController.loanApplicationCharacters(TRANSACTION_ID, ACCOUNT_ID, 30, 10);

        verify(charactersPort, times(1)).loanApplicationCharacters(ACCOUNT_ID, 10, 30, TRANSACTION_ID);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetCharacter_Success() {
        CharacterModel characterModel = CharacterModel.builder().build();

        CharacterDetailDto characterDetailDto = new CharacterDetailDto(characterModel);
        when(charactersPort.getCharacter(CHARACTER_ID, ACCOUNT_ID, TRANSACTION_ID)).thenReturn(characterDetailDto);

        ResponseEntity<GenericResponse<CharacterDetailDto>> response =
                charactersController.character(TRANSACTION_ID, ACCOUNT_ID, CHARACTER_ID);

        verify(charactersPort, times(1)).getCharacter(CHARACTER_ID, ACCOUNT_ID, TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetCharacter_NoContent() {
        when(charactersPort.getCharacter(CHARACTER_ID, ACCOUNT_ID, TRANSACTION_ID)).thenReturn(null);

        ResponseEntity<GenericResponse<CharacterDetailDto>> response =
                charactersController.character(TRANSACTION_ID, ACCOUNT_ID, CHARACTER_ID);

        verify(charactersPort, times(1)).getCharacter(CHARACTER_ID, ACCOUNT_ID, TRANSACTION_ID);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testInventory_Success() {
        List<CharacterInventoryModel> inventory = List.of(new CharacterInventoryModel(1L, 1L, 1L, 1L, 1L, 12L));
        when(charactersPort.inventory(CHARACTER_ID, ACCOUNT_ID, TRANSACTION_ID)).thenReturn(inventory);

        ResponseEntity<GenericResponse<List<CharacterInventoryModel>>> response =
                charactersController.inventory(TRANSACTION_ID, ACCOUNT_ID, CHARACTER_ID);

        verify(charactersPort, times(1)).inventory(CHARACTER_ID, ACCOUNT_ID, TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getData().isEmpty());
    }

    @Test
    void testTransferInventoryItem_Success() {
        TransferItemRequest request = new TransferItemRequest();
        request.setCharacterId(CHARACTER_ID);
        request.setAccountId(ACCOUNT_ID);
        request.setFriendId(FRIEND_ID);
        request.setItemId(ITEM_ID);
        request.setQuantity(2);

        ResponseEntity<GenericResponse<Void>> response =
                charactersController.transferInventoryItem(TRANSACTION_ID, request);

        verify(charactersPort, times(1)).transferInventoryItem(CHARACTER_ID, ACCOUNT_ID, FRIEND_ID, ITEM_ID, 2,
                TRANSACTION_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
