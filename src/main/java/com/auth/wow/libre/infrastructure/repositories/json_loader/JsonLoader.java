package com.auth.wow.libre.infrastructure.repositories.json_loader;

import com.auth.wow.libre.domain.model.CountryModel;
import com.auth.wow.libre.domain.ports.out.resources.JsonLoaderPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JsonLoader implements JsonLoaderPort {
  private final ObjectMapper objectMapper;

  private final Resource jsonFile;

  private List<CountryModel> jsonCountryModel;


  public JsonLoader(ObjectMapper objectMapper,
                    @Value("classpath:/static/countryAvailable.json") Resource jsonFile) {
    this.objectMapper = objectMapper;
    this.jsonFile = jsonFile;
  }

  @PostConstruct
  public void loadJsonFile() {
    try {
      jsonCountryModel = objectMapper.readValue(jsonFile.getInputStream(), new TypeReference<>() {
      });

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CountryModel> getJsonCountry(String transactionId) {
    return jsonCountryModel;
  }


}
