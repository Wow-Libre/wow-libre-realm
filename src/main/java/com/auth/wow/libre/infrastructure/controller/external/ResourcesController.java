package com.auth.wow.libre.infrastructure.controller.external;

import com.auth.wow.libre.domain.model.Country;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.GenericResponseBuilder;
import com.auth.wow.libre.domain.ports.in.resources.ResourcesPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

@RestController
@RequestMapping("/api/resources")
public class ResourcesController {

  private final ResourcesPort resourcesPort;

  public ResourcesController(ResourcesPort resourcesPort) {
    this.resourcesPort = resourcesPort;
  }

  @GetMapping("/country")
  public ResponseEntity<GenericResponse<List<Country>>> getCountry(
          @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {
    List<Country> countryList = resourcesPort.getCountry(transactionId);
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(new GenericResponseBuilder<>(countryList, transactionId).ok().build());
  }
}
