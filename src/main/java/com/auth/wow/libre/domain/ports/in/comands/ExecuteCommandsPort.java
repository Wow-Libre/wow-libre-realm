package com.auth.wow.libre.domain.ports.in.comands;

import jakarta.xml.bind.*;


public interface ExecuteCommandsPort {
    void execute(String messageEncrypt, byte[] salt, String transactionId) throws Exception;

    void execute(String command, String transactionId) throws JAXBException;

}
