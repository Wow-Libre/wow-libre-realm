package com.auth.wow.libre.domain.ports.out.product;

import com.auth.wow.libre.infrastructure.entities.ProductEntity;

import java.util.List;

public interface ObtainProductPort {
  List<ProductEntity> findAll(String transactionId);
}
