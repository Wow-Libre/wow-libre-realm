package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.character_inventory.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.item_instance.*;
import com.auth.wow.libre.domain.ports.out.character_inventory.*;
import com.auth.wow.libre.domain.ports.out.item_template.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterInventoryServiceTest {
    @Mock
    private ItemInstancePort instancePort;

    @Mock
    private ObtainCharacterInventory obtainCharacterInventory;

    @Mock
    private ObtainItemTemplate obtainItemTemplate;

    @InjectMocks
    private CharacterInventoryService characterInventoryService;

    private static final Long CHARACTER_ID = 100L;
    private static final Long ITEM_ID = 200L;
    private static final String TRANSACTION_ID = "tx-123";

    @BeforeEach
    void setUp() {
        characterInventoryService = new CharacterInventoryService(instancePort, obtainCharacterInventory,
                obtainItemTemplate);
    }


    @Test
    void testDelete_Success() {
        CharacterInventoryEntity inventoryEntity = mock(CharacterInventoryEntity.class);
        when(inventoryEntity.getItem()).thenReturn(ITEM_ID);
        when(inventoryEntity.getGuid()).thenReturn(500L);
        when(obtainCharacterInventory.findByGuid(CHARACTER_ID, ITEM_ID, TRANSACTION_ID)).thenReturn(Optional.of(inventoryEntity));

        characterInventoryService.delete(CHARACTER_ID, ITEM_ID, TRANSACTION_ID);

        verify(instancePort, times(1)).delete(ITEM_ID, 500L, TRANSACTION_ID);
    }

    @Test
    void testDelete_ItemNotFound() {
        when(obtainCharacterInventory.findByGuid(CHARACTER_ID, ITEM_ID, TRANSACTION_ID)).thenReturn(Optional.empty());

        InternalException exception = assertThrows(InternalException.class, () -> {
            characterInventoryService.delete(CHARACTER_ID, ITEM_ID, TRANSACTION_ID);
        });

        assertEquals("ItemInstance not found for GUID: ", exception.getMessage());
    }


}
