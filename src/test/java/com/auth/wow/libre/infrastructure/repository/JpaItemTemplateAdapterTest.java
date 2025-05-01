package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.world.*;
import com.auth.wow.libre.infrastructure.repositories.world.item_template.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaItemTemplateAdapterTest {

    @Mock
    private ItemTemplateRepository itemTemplateRepository;

    @InjectMocks
    private JpaItemTemplateAdapter jpaItemTemplateAdapter;

    @Test
    void findByEntry_ShouldReturnItemTemplateEntity_WhenExists() {
        Long entry = 123L;
        ItemTemplateEntity item = new ItemTemplateEntity();
        when(itemTemplateRepository.findByEntry(entry)).thenReturn(Optional.of(item));

        Optional<ItemTemplateEntity> result = jpaItemTemplateAdapter.findByEntry(entry);

        assertTrue(result.isPresent());
        verify(itemTemplateRepository, times(1)).findByEntry(entry);
    }

    @Test
    void findByEntry_ShouldReturnEmpty_WhenNotExists() {
        Long entry = 999L;
        when(itemTemplateRepository.findByEntry(entry)).thenReturn(Optional.empty());

        Optional<ItemTemplateEntity> result = jpaItemTemplateAdapter.findByEntry(entry);

        assertFalse(result.isPresent());
        verify(itemTemplateRepository, times(1)).findByEntry(entry);
    }

    @Test
    void findRandomEntry_ShouldReturnItemTemplateEntity_WhenRepositoryReturnsNonEmptyList() {
        ItemTemplateEntity item = new ItemTemplateEntity();
        List<ItemTemplateEntity> itemList = Collections.singletonList(item);
        when(itemTemplateRepository.findRandomItem()).thenReturn(itemList);

        Optional<ItemTemplateEntity> result = jpaItemTemplateAdapter.findRandomEntry();

        assertTrue(result.isPresent());
        assertEquals(item, result.get());
        verify(itemTemplateRepository, times(1)).findRandomItem();
    }

    @Test
    void findRandomEntry_ShouldReturnEmpty_WhenRepositoryReturnsEmptyList() {
        when(itemTemplateRepository.findRandomItem()).thenReturn(Collections.emptyList());

        Optional<ItemTemplateEntity> result = jpaItemTemplateAdapter.findRandomEntry();

        assertFalse(result.isPresent());
        verify(itemTemplateRepository, times(1)).findRandomItem();
    }

}
