package com.auth.wow.libre.infrastructure.client.soap.xml;

import org.springframework.ws.client.support.interceptor.*;
import org.springframework.ws.context.*;

import java.io.*;

public class CustomLoggingInterceptor implements ClientInterceptor {
    @Override
    public boolean handleRequest(MessageContext messageContext) {
        // Log de la solicitud SOAP
        System.out.println("Request XML:");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            messageContext.getRequest().writeTo(os);
            System.out.println(new String(os.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(); // Salto de línea para claridad
        return true; // Continuar con la ejecución normal
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {
        // Log de la respuesta SOAP
        System.out.println("Response XML:");
        try {
            messageContext.getResponse().writeTo(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(); // Salto de línea para claridad
        return true; // Continuar con la ejecución normal
    }

    @Override
    public boolean handleFault(MessageContext messageContext) {
        // Log de fallos SOAP, si es necesario
        return true; // Continuar con la ejecución normal
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) {
        // Lógica adicional después de completar la solicitud
    }
}
