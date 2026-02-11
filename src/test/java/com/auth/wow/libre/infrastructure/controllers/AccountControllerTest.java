package com.auth.wow.libre.infrastructure.controllers;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class AccountControllerTest {

    private AccountController accountController;

    @Mock
    private AccountPort accountPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountController = new AccountController(accountPort);
    }

    @Test
    void testCreateAccount() {
        when(accountPort.create(any(), any(), any(), any(), any(), any(), any())).thenReturn(1L);
        CreateAccountDto request = new CreateAccountDto("user", "pass", "email", 1L, 2);
        ResponseEntity<GenericResponse<Long>> response = accountController.create("12345",
                EmulatorCore.AZEROTH_CORE.getName(), request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getData());
    }

    @Test
    void testGetAccountById() {
        AccountBanned mockBanned = new AccountBanned(1L, new Date(), new Date(), "Admin",
                "Reason", true);
        AccountDetailDto accountDetail = new AccountDetailDto(
                1L,
                "user",
                "email@example.com",
                "expansion",
                true,
                "0",
                LocalDate.now(),
                "127.0.0.1",
                "None",
                "Admin",
                false,
                LocalDate.now(),
                "Windows",
                mockBanned
        );
        when(accountPort.account(anyLong(), any())).thenReturn(accountDetail);
        ResponseEntity<GenericResponse<AccountDetailDto>> response = accountController.account("12345", 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("user", response.getBody().getData().username());
    }

    @Test
    void testGetAccountByIdNotFound() {
        when(accountPort.account(anyLong(), any())).thenReturn(null);
        ResponseEntity<GenericResponse<AccountDetailDto>> response = accountController.account("12345", 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testChangePassword() {
        doNothing().when(accountPort).changePassword(anyLong(), anyLong(), any(), any(), any(), any());
        ChangePasswordAccountDto request = new ChangePasswordAccountDto();
        request.setPassword("newPass");
        request.setAccountId(1L);
        request.setUserId(22L);
        request.setExpansionId(2);

        ResponseEntity<GenericResponse<Void>> response = accountController.changePassword("12345",
                EmulatorCore.AZEROTH_CORE.getName(), request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllAccounts() {
        AccountsDto accountsDto = getBuildAccountsDto();
        when(accountPort.accounts(anyInt(), anyInt(), any(), any())).thenReturn(accountsDto);

        ResponseEntity<GenericResponse<AccountsDto>> response = accountController.accounts("12345", 10, 1, "test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().getAccounts().size());
    }

    @Test
    void testGetAllAccountsByNull() {
        when(accountPort.accounts(anyInt(), anyInt(), any(), any())).thenReturn(null);
        ResponseEntity<GenericResponse<AccountsDto>> response = accountController.accounts("12345", 10, 1, "test");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    private AccountsDto getBuildAccountsDto() {
        List<AccountsServerDto> accounts = new ArrayList<>();
        accounts.add(new AccountsServerDto(1L, "testUser", "test@example.com", "expansion", true, "0",
                LocalDate.now(), "127.0.0.1", "None", "Admin", false, LocalDate.now(), "Windows", false));

        return new AccountsDto(accounts, 1L);
    }
}
