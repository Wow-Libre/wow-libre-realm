package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import jakarta.validation.*;
import jakarta.xml.bind.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/commands")
public class CommandsController {

    private final ExecuteCommandsPort executeCommandsPort;

    public CommandsController(ExecuteCommandsPort executeCommandsPort) {
        this.executeCommandsPort = executeCommandsPort;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Void>> commands(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_EMULATOR) final String emulator,
            @RequestBody @Valid ExecuteCommandRequest request) throws JAXBException {

        executeCommandsPort.execute(request.getMessage(), EmulatorCore.valueOf(emulator), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }


}
