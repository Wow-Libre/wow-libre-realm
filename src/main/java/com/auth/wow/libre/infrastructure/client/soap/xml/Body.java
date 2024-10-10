package com.auth.wow.libre.infrastructure.client.soap.xml;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "executeCommand"
})

public class Body {

    @XmlElement(name = "executeCommand", namespace = "urn:TC", required = true)
    protected ExecuteCommand executeCommand;

    public ExecuteCommand getExecuteCommand() {
        return executeCommand;
    }

    public void setExecuteCommand(ExecuteCommand value) {
        this.executeCommand = value;
    }
}
