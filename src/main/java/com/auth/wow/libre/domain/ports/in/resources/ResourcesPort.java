package com.auth.wow.libre.domain.ports.in.resources;

import com.auth.wow.libre.domain.model.Benefit;
import com.auth.wow.libre.domain.model.Country;

import java.util.List;

public interface ResourcesPort {
  List<Country> getCountry(String transactionId);
  List<Benefit> getBenefits(String transactionId);
}
