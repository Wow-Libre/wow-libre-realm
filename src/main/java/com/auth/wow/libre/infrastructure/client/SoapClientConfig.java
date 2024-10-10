package com.auth.wow.libre.infrastructure.client;

import com.auth.wow.libre.infrastructure.client.soap.xml.*;
import org.springframework.context.annotation.*;
import org.springframework.oxm.jaxb.*;
import org.springframework.ws.client.core.*;
import org.springframework.ws.client.support.interceptor.*;

@Configuration
public class SoapClientConfig {
    @Bean
    public Jaxb2Marshaller requestMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.auth.wow.libre.infrastructure.client.soap.xml");
        return marshaller;
    }

    @Bean
    public Jaxb2Marshaller responseUnmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setContextPath("com.auth.wow.libre.infrastructure.client.soap.xml.resp");
        return unmarshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller requestMarshaller,
                                                 Jaxb2Marshaller responseUnmarshaller,
                                                 WebServiceMessageSenderWithAuth auth) {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(requestMarshaller);
        webServiceTemplate.setUnmarshaller(responseUnmarshaller);
        webServiceTemplate.setMessageSender(auth);
        webServiceTemplate.setInterceptors(new ClientInterceptor[]{new CustomLoggingInterceptor()});

        webServiceTemplate.setDefaultUri("http://127.0.0.1:7878");
        return webServiceTemplate;
    }
}