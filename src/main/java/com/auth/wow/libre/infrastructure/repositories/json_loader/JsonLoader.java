package com.auth.wow.libre.infrastructure.repositories.json_loader;

import com.auth.wow.libre.domain.model.Country;
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
  private List<Country> jsonCountry;

  public JsonLoader(ObjectMapper objectMapper, @Value("classpath:/static/countrys.json") Resource jsonFile) {
    this.objectMapper = objectMapper;
    this.jsonFile = jsonFile;
  }

  @PostConstruct
  public void loadJsonFile() {
    try {
      jsonCountry = objectMapper.readValue(jsonFile.getInputStream(), new TypeReference<List<Country>>() {
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Country> getJsonCountry(String transactionId) {
    return jsonCountry;
  }
}
