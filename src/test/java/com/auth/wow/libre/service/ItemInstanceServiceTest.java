package com.auth.wow.libre.service;

import com.auth.wow.libre.application.services.item_instance.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.out.item_instance.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemInstanceServiceTest {

    @Mock
    private DeleteItemInstance deleteItemInstance;

    @Mock
    private ObtainItemInstance obtainItemInstance;

    @InjectMocks
    private ItemInstanceService itemInstanceService;

    private static final Long GUID = 1L;
    private static final Long CHARACTER_ID = 100L;
    private static final String TRANSACTION_ID = "tx-123";

    @BeforeEach
    void setUp() {
        itemInstanceService = new ItemInstanceService(deleteItemInstance, obtainItemInstance);
    }

    @Test
    void testDelete_Success() {
        ItemInstanceEntity itemInstance = mock(ItemInstanceEntity.class);
        when(obtainItemInstance.findByGuidAndOwnerGuid(GUID, CHARACTER_ID, TRANSACTION_ID)).thenReturn(Optional.of(itemInstance));

        itemInstanceService.delete(GUID, CHARACTER_ID, TRANSACTION_ID);

        verify(deleteItemInstance, times(1)).delete(itemInstance, TRANSACTION_ID);
    }

    @Test
    void testDelete_ItemNotFound() {
        when(obtainItemInstance.findByGuidAndOwnerGuid(GUID, CHARACTER_ID, TRANSACTION_ID)).thenReturn(Optional.empty());

        InternalException exception = assertThrows(InternalException.class, () -> {
            itemInstanceService.delete(GUID, CHARACTER_ID, TRANSACTION_ID);
        });

        assertEquals("ItemInstance not found for GUID: " + GUID, exception.getMessage());
    }

    @Test
    void testFindByGuidAndOwnerGuid() {
        ItemInstanceEntity itemInstance = mock(ItemInstanceEntity.class);
        when(obtainItemInstance.findByGuidAndOwnerGuid(GUID, CHARACTER_ID, TRANSACTION_ID)).thenReturn(Optional.of(itemInstance));

        Optional<ItemInstanceEntity> result = itemInstanceService.findByGuidAndOwnerGuid(GUID, CHARACTER_ID,
                TRANSACTION_ID);

        assertTrue(result.isPresent());
        assertEquals(itemInstance, result.get());
    }
}
