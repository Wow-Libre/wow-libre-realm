package com.auth.wow.libre.application.services.product;

import com.auth.wow.libre.domain.model.Product;
import com.auth.wow.libre.domain.ports.in.product.ProductPort;
import com.auth.wow.libre.domain.ports.out.product.LoadProductPort;
import com.auth.wow.libre.infrastructure.entities.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductPort {

  private final LoadProductPort loadProductPort;

  public ProductService(LoadProductPort loadProductPort) {
    this.loadProductPort = loadProductPort;
  }

  @Override
  public List<Product> getProducts(String transactionId) {
    return loadProductPort.findAll(transactionId).stream().map(this::mapProductEntityToProduct).collect(Collectors.toList());
  }

  private Product mapProductEntityToProduct(ProductEntity productEntity) {
    return new Product(
        productEntity.getId(),
        productEntity.getName(),
        productEntity.getDescription(),
        productEntity.getImage(),
        productEntity.getPrice(),
        productEntity.getDiscount(),
        productEntity.getReferenceNumber(),
        productEntity.getItemCode()
    );
  }
}
