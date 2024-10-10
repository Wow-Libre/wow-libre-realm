package com.auth.wow.libre.infrastructure.client.soap.xml.resp;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"result"}
)
/*  TrinityCore
@XmlRootElement(
        name = "executeCommandResponse", namespace = "urn:TC"
)
*/
/* AzerothCore*/
@XmlRootElement(name = "executeCommandResponse", namespace = "urn:AC")
@Data
public class ExecuteCommandResponse {
    @XmlElement(
            required = true
    )
    protected String result;

    public ExecuteCommandResponse() {
    }
}
