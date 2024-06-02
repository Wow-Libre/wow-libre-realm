package com.auth.wow.libre.domain.ports.out.resources;

import com.auth.wow.libre.domain.model.CountryModel;

import java.util.List;

public interface JsonLoaderPort {
    List<CountryModel> getJsonCountry(String transactionId);
}
