package com.auth.wow.libre.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "product")
public class ProductEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  @Column(name = "img_url")
  private String image;
  private Double price;
  private Double discount;
  private boolean status;
  @Column(name = "reference_number")
  private String referenceNumber;
  @Column(name = "item_code")
  private String itemCode;
}
