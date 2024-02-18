package com.auth.wow.libre.domain.ports.in.product;

import com.auth.wow.libre.domain.model.Product;

import java.util.List;

public interface ProductPort {
  List<Product> getProducts(String transactionId);
}
