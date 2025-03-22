package com.auth.wow.libre.infrastructure.repository;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import com.auth.wow.libre.infrastructure.repositories.characters.mail.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaMailAdapterTest {
    @Mock
    private MailRepository mailRepository;

    @InjectMocks
    private JpaMailAdapter jpaMailAdapter;

    private MailEntity mailEntity;
    private static final Long CHARACTER_ID = 100L;
    private static final Long MAIL_ID = 200L;
    private static final String TRANSACTION_ID = "txn-123";

    @BeforeEach
    void setUp() {
        mailEntity = new MailEntity();
    }

    @Test
    void testFindByMailGuidId() {
        when(mailRepository.findByReceiverGuidId(CHARACTER_ID)).thenReturn(List.of(mailEntity));

        List<MailEntity> result = jpaMailAdapter.findByMailGuidId(CHARACTER_ID, TRANSACTION_ID);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mailEntity, result.get(0));
        verify(mailRepository, times(1)).findByReceiverGuidId(CHARACTER_ID);
    }

    @Test
    void testFindByItemsAndMailId() {
        when(mailRepository.findByMailsAndItems(MAIL_ID)).thenReturn(List.of());

        List<MailEntityModel> result = jpaMailAdapter.findByItemsAndMailId(MAIL_ID, TRANSACTION_ID);

        assertTrue(result.isEmpty());
        verify(mailRepository, times(1)).findByMailsAndItems(MAIL_ID);
    }
}
