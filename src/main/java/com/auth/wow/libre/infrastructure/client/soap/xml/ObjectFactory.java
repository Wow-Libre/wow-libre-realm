package com.auth.wow.libre.infrastructure.client.soap.xml;

import jakarta.xml.bind.annotation.*;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public Envelope createEnvelope2() {
        return new Envelope();
    }

    public Body createBody2() {
        return new Body();
    }

    public ExecuteCommand createExecuteCommand2() {
        return new ExecuteCommand();
    }

}
