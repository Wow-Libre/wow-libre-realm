package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.character_inventory.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaCharacterInventoryAdapterTest {
    @Mock
    private CharacterInventoryRepository characterInventoryRepository;

    @InjectMocks
    private JpaCharacterInventoryAdapter jpaCharacterInventoryAdapter;

    private CharacterInventoryEntity characterInventoryEntity;
    private CharacterInventoryModel characterInventoryModel;
    private static final Long GUID = 1L;
    private static final Long ITEM_ID = 100L;
    private static final String TRANSACTION_ID = "txn-123";

    @BeforeEach
    void setUp() {
        characterInventoryEntity = new CharacterInventoryEntity();
        characterInventoryModel = new CharacterInventoryModel(1L, 2L, 32L, 23L, 2L, 11L);
    }

    @Test
    void testDelete() {
        jpaCharacterInventoryAdapter.delete(characterInventoryEntity, TRANSACTION_ID);
        verify(characterInventoryRepository, times(1)).delete(characterInventoryEntity);
    }

    @Test
    void testFindByGuidModel() {
        when(characterInventoryRepository.findByGuidAndItemModel(GUID, ITEM_ID)).thenReturn(Optional.of(characterInventoryModel));
        Optional<CharacterInventoryModel> result = jpaCharacterInventoryAdapter.findByGuidModel(GUID, ITEM_ID,
                TRANSACTION_ID);
        assertTrue(result.isPresent());
        assertEquals(characterInventoryModel, result.get());
        verify(characterInventoryRepository, times(1)).findByGuidAndItemModel(GUID, ITEM_ID);
    }

    @Test
    void testFindByGuid() {
        when(characterInventoryRepository.findByGuidAndItem(GUID, ITEM_ID)).thenReturn(Optional.of(characterInventoryEntity));
        Optional<CharacterInventoryEntity> result = jpaCharacterInventoryAdapter.findByGuid(GUID, ITEM_ID,
                TRANSACTION_ID);
        assertTrue(result.isPresent());
        assertEquals(characterInventoryEntity, result.get());
        verify(characterInventoryRepository, times(1)).findByGuidAndItem(GUID, ITEM_ID);
    }

    @Test
    void testFindByAllInventory() {
        List<CharacterInventoryModel> expectedList = List.of(characterInventoryModel);
        when(characterInventoryRepository.findByAllInventory(GUID)).thenReturn(expectedList);
        List<CharacterInventoryModel> result = jpaCharacterInventoryAdapter.findByAllInventory(GUID, TRANSACTION_ID);
        assertFalse(result.isEmpty());
        assertEquals(expectedList, result);
        verify(characterInventoryRepository, times(1)).findByAllInventory(GUID);
    }
}
