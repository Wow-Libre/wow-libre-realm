package com.auth.wow.libre.application.services.product;

import com.auth.wow.libre.domain.model.Product;
import com.auth.wow.libre.domain.model.ProductCategory;
import com.auth.wow.libre.domain.ports.in.product.ProductPort;
import com.auth.wow.libre.domain.ports.out.product.ObtainProductPort;
import com.auth.wow.libre.infrastructure.entities.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ProductPort {

    private final ObtainProductPort obtainProductPort;

    public ProductService(ObtainProductPort obtainProductPort) {
        this.obtainProductPort = obtainProductPort;
    }

    @Override
    public List<ProductCategory> getProducts(String transactionId) {
        List<Product> products = obtainProductPort.findAll(transactionId).stream().map(this::mapProductEntityToProduct).toList();
        return null;
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
                productEntity.getItemCode(),
                productEntity.getCategory()
        );
    }
}
