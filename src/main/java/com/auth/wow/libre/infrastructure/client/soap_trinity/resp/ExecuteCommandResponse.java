package com.auth.wow.libre.infrastructure.client.soap_trinity.resp;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"result"}
)
@XmlRootElement(
        name = "executeCommandResponse", namespace = "urn:TC"
)
@Data
public class ExecuteCommandResponse {
    @XmlElement(
            required = true
    )
    protected String result;

    public ExecuteCommandResponse() {
    }
}
