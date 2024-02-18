package com.auth.wow.libre.infrastructure.repositories.product;

import com.auth.wow.libre.infrastructure.entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
  List<ProductEntity> findByStatusIsTrue();
}
