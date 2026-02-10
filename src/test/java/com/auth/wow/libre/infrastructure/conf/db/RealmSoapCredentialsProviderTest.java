package com.auth.wow.libre.infrastructure.conf.db;

import com.auth.wow.libre.infrastructure.conf.RealmProperties;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("RealmSoapCredentialsProvider")
class RealmSoapCredentialsProviderTest {

    private RealmProperties realmProperties;
    private RealmDataSourceRegistry registry;
    private RealmSoapCredentialsProvider provider;

    @BeforeEach
    void setUp() {
        realmProperties = new RealmProperties();
        registry = mock(RealmDataSourceRegistry.class);
        when(registry.getDefaultRealmId()).thenReturn(1L);
        provider = new RealmSoapCredentialsProvider(realmProperties, registry);
        ReflectionTestUtils.setField(provider, "defaultSoapUri", "http://default:7878");
    }

    @Test
    void getUri_ReturnsRealmUri_WhenRealmHasSoapUriConfigured() {
        RealmProperties.RealmConfig realm = new RealmProperties.RealmConfig();
        realm.setId(1L);
        RealmProperties.SoapCredentials soap = new RealmProperties.SoapCredentials();
        soap.setUri("http://realm1:7878");
        soap.setUsername("user");
        soap.setPassword("pass");
        realm.setSoap(soap);
        realmProperties.setRealms(List.of(realm));

        String uri = provider.getUri(1L);

        assertEquals("http://realm1:7878", uri);
    }

    @Test
    void getUri_ReturnsDefaultUri_WhenRealmHasNoSoapUri() {
        RealmProperties.RealmConfig realm = new RealmProperties.RealmConfig();
        realm.setId(1L);
        RealmProperties.SoapCredentials soap = new RealmProperties.SoapCredentials();
        soap.setUsername("user");
        soap.setPassword("pass");
        realm.setSoap(soap);
        realmProperties.setRealms(List.of(realm));

        String uri = provider.getUri(1L);

        assertEquals("http://default:7878", uri);
    }

    @Test
    void getUri_ReturnsDefaultUri_WhenRealmIdIsNull_UsesDefaultRealm() {
        RealmProperties.RealmConfig realm = new RealmProperties.RealmConfig();
        realm.setId(1L);
        RealmProperties.SoapCredentials soap = new RealmProperties.SoapCredentials();
        soap.setUri("http://realm1:7878");
        soap.setUsername("user");
        soap.setPassword("pass");
        realm.setSoap(soap);
        realmProperties.setRealms(List.of(realm));

        String uri = provider.getUri(null);

        assertEquals("http://realm1:7878", uri);
        verify(registry, times(1)).getDefaultRealmId();
    }

    @Test
    void getUri_ReturnsDefaultUri_WhenNoRealmConfigFound() {
        realmProperties.setRealms(List.of());

        String uri = provider.getUri(999L);

        assertEquals("http://default:7878", uri);
    }

    @Test
    void getCredentials_Throws_WhenRealmHasNoSoapCredentials() {
        RealmProperties.RealmConfig realm = new RealmProperties.RealmConfig();
        realm.setId(1L);
        realm.setSoap(null);
        realmProperties.setRealms(List.of(realm));

        assertThrows(IllegalStateException.class, () -> provider.getCredentials(1L));
    }

    @Test
    void getCredentials_ReturnsCredentials_WhenRealmHasSoapConfigured() {
        RealmProperties.RealmConfig realm = new RealmProperties.RealmConfig();
        realm.setId(1L);
        RealmProperties.SoapCredentials soap = new RealmProperties.SoapCredentials();
        soap.setUsername("gmuser");
        soap.setPassword("gmpass");
        realm.setSoap(soap);
        realmProperties.setRealms(List.of(realm));

        var creds = provider.getCredentials(1L);

        assertEquals("gmuser", creds.username());
        assertEquals("gmpass", creds.password());
    }
}
