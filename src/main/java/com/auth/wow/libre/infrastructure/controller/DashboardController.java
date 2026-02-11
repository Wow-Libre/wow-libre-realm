package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.dashboard.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardPort dashboardPort;

    public DashboardController(DashboardPort dashboardPort) {
        this.dashboardPort = dashboardPort;
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<GenericResponse<DashboardMetricsDto>> stats(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {

        DashboardMetricsDto metricsCount = dashboardPort.metricsCount(transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<DashboardMetricsDto>(transactionId).ok(metricsCount).build());
    }

    @PutMapping("/account/email")
    public ResponseEntity<GenericResponse<Void>> updateMailAccount(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid AccountUpdateMailDto request) {

        dashboardPort.updateMailAccount(request.getUsername(),
                request.getMail(), transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PostMapping("/account/ban")
    public ResponseEntity<GenericResponse<Void>> accountBan(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid AccountBanDto request) {

        dashboardPort.bannedUser(request, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


    @PostMapping(path = "/emulator-config")
    public ResponseEntity<GenericResponse<Map<String, String>>> emulatorConfiguration(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid EmulatorConfigDto request) {

        Map<String, String> response = dashboardPort.getFileConfig(request.getRoute(),
                transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Map<String, String>>(transactionId).ok(response).build());
    }


    @PutMapping(path = "/emulator-config")
    public ResponseEntity<GenericResponse<Void>> emulatorConfiguration(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid EmulatorUpdateConfigDto request) {

        dashboardPort.updateFileConfig(request.getRoute(), request.getConfigs(), transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


    @GetMapping("/routes/emulators")
    public ResponseEntity<GenericResponse<EmulatorRoutesDto>> getEmulatorServerRoutes(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {
        EmulatorRoutesDto emulatorRoutesDto = dashboardPort.getEmulatorRoutes(transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(emulatorRoutesDto, transactionId).ok().build());
    }
}
