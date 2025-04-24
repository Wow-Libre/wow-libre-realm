package com.auth.wow.libre.infrastructure.client.soap_trinity;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "body"
})
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class Envelope {

    @XmlElement(name = "Body", required = true, namespace = "urn:TC")
    protected Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body value) {
        this.body = value;
    }
}
