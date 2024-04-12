package com.auth.wow.libre.domain.ports.in.product;

import com.auth.wow.libre.domain.model.ProductCategory;

import java.util.List;

public interface ProductPort {
  List<ProductCategory> getProducts(String transactionId);
}
