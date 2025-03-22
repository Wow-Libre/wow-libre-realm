package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.character_social.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaCharacterSocialAdapterTest {
    @Mock
    private CharacterSocialRepository characterSocialRepository;

    @InjectMocks
    private JpaCharacterSocialAdapter jpaCharacterSocialAdapter;

    private CharacterSocialEntity characterSocialEntity;
    private static final Long GUID = 1L;
    private static final Long FRIEND_GUID = 2L;
    private static final String TRANSACTION_ID = "txn-123";

    @BeforeEach
    void setUp() {
        characterSocialEntity = new CharacterSocialEntity();
        characterSocialEntity.setGuid(GUID);
        characterSocialEntity.setFriend(FRIEND_GUID);
    }

    @Test
    void testGetFriends() {
        when(characterSocialRepository.findByGuid(GUID)).thenReturn(List.of(characterSocialEntity));

        List<CharacterSocial> result = jpaCharacterSocialAdapter.getFriends(GUID, TRANSACTION_ID);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(FRIEND_GUID, result.get(0).friend);
        verify(characterSocialRepository, times(1)).findByGuid(GUID);
    }

    @Test
    void testDeleteFriend() {
        when(characterSocialRepository.findByGuid(GUID)).thenReturn(List.of(characterSocialEntity));

        jpaCharacterSocialAdapter.deleteFriend(GUID, FRIEND_GUID, TRANSACTION_ID);

        verify(characterSocialRepository, times(1)).delete(characterSocialEntity);
    }
}
