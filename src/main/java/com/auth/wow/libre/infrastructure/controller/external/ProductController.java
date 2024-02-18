package com.auth.wow.libre.infrastructure.controller.external;

import com.auth.wow.libre.domain.model.Product;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.GenericResponseBuilder;
import com.auth.wow.libre.domain.ports.in.product.ProductPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;

@RestController
@RequestMapping("/api/store/products")
public class ProductController {
  private final ProductPort productPort;

  public ProductController(ProductPort productPort) {
    this.productPort = productPort;
  }


  @GetMapping
  public ResponseEntity<GenericResponse<List<Product>>> products(
      @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {
    List<Product> products = productPort.getProducts(transactionId);

    if (products != null) {
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(new GenericResponseBuilder<List<Product>>(transactionId).ok(products).build());
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
