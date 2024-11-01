package com.auth.wow.libre.infrastructure.controller;

import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import jakarta.validation.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@RestController
@RequestMapping("/commands")
public class CommandsController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CommandsController.class);

    private final ExecuteCommandsPort executeCommandsPort;

    public CommandsController(ExecuteCommandsPort executeCommandsPort) {
        this.executeCommandsPort = executeCommandsPort;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Void>> commands(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid ExecuteCommandRequest request) throws Exception {

        executeCommandsPort.execute(request.getMessage(), request.getSalt(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }

}
