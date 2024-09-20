package com.auth.wow.libre.domain.ports.out.resources;

import com.auth.wow.libre.domain.model.CountryModel;
import com.auth.wow.libre.domain.model.FaqsModel;
import com.auth.wow.libre.domain.model.PlanModel;

import java.util.List;

public interface JsonLoaderPort {
    List<CountryModel> getJsonCountry(String transactionId);

    List<FaqsModel> getJsonFaqs(String transactionId);

    List<PlanModel> getJsonPlans(String language, String transactionId);

}
