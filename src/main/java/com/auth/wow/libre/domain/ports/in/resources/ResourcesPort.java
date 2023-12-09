package com.auth.wow.libre.domain.ports.in.resources;

import com.auth.wow.libre.domain.model.Country;

import java.util.List;

public interface ResourcesPort {
  List<Country> getCountry(String transactionId);
}
