package com.auth.wow.libre.domain.ports.out.product;

import com.auth.wow.libre.domain.model.dto.ProductDto;

public interface LoadProductPort {
  void loadProduct(ProductDto product, String transactionId);
}
