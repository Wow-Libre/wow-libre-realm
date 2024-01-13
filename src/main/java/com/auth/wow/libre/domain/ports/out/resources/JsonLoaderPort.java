package com.auth.wow.libre.domain.ports.out.resources;

import com.auth.wow.libre.domain.model.Benefit;
import com.auth.wow.libre.domain.model.Country;

import java.util.List;

public interface JsonLoaderPort {
  List<Country>  getJsonCountry(String transactionId);
  List<Benefit> getJsonBenefits(String transactionId);
}
