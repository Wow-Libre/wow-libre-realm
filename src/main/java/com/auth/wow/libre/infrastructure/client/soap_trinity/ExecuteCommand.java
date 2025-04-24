package com.auth.wow.libre.infrastructure.client.soap_trinity;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)

@XmlRootElement(
        name = "executeCommand", namespace = "urn:TC"
)
public class ExecuteCommand {


    protected String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String value) {
        this.command = value;
    }
}
