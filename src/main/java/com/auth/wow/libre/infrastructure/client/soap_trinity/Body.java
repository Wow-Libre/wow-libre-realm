package com.auth.wow.libre.infrastructure.client.soap_trinity;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "executeCommand"
})
@XmlRootElement(name = "Envelope", namespace = "urn:TC")
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
