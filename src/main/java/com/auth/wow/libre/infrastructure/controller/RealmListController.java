package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.realmlist.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/api/realmlist")
public class RealmListController {
    private final RealmlistPort realmlistPort;

    public RealmListController(RealmlistPort realmlistPort) {
        this.realmlistPort = realmlistPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<RealmlistDto>>> realmList(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {

        List<RealmlistDto> realmlist = realmlistPort.findByAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<List<RealmlistDto>>(transactionId).ok(realmlist).build());
    }
}
