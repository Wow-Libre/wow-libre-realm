package com.auth.wow.libre.domain.ports.in.resources;

import com.auth.wow.libre.domain.model.Benefit;
import com.auth.wow.libre.domain.model.CountryModel;

import java.util.List;

public interface ResourcesPort {
  List<CountryModel> getCountry(String transactionId);
}
