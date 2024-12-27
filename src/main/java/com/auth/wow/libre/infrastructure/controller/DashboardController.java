package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.dashboard.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

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
}
