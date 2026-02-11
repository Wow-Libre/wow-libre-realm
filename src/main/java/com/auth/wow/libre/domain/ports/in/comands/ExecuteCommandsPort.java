package com.auth.wow.libre.domain.ports.in.comands;

import com.auth.wow.libre.domain.model.enums.*;
import jakarta.xml.bind.*;


public interface ExecuteCommandsPort {
    void execute(String command, EmulatorCore core, String transactionId) throws JAXBException;
}
