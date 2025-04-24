package com.auth.wow.libre.infrastructure.client.soap_trinity.resp;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"body"}
)
@XmlRootElement(
        name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/"
)
@Data
public class Envelope {
    @XmlElement(
            name = "Body",
            required = true
    )
    protected Body body;

    public Envelope() {
    }

    @XmlAccessorType(XmlAccessType.FIELD)

    @XmlType(
            name = "",
            propOrder = {"executeCommandResponse"}
    )
    @Data
    public static class Body {
        /* TrinityCore
        @XmlElement(
                @XmlElement(name = "executeCommandResponse", namespace = "urn:AC", required = true)
                required = true, namespace = "urn:TC"
        )
         */
        // AzerothCore
        @XmlElement(name = "executeCommandResponse", namespace = "urn:TC", required = true)
        protected ExecuteCommandResponse executeCommandResponse;

        public Body() {
        }
    }
}