package com.auth.wow.libre.infrastructure.client.soap.xml;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
/*  TrinityCore
@XmlRootElement(
        name = "executeCommandResponse", namespace = "urn:TC"
)
 */
/* AzerothCore*/
@XmlRootElement(name = "executeCommand", namespace = "urn:AC")
public class ExecuteCommand {


    protected String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String value) {
        this.command = value;
    }
}
