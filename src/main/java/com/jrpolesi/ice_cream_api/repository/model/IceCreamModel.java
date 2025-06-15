package com.jrpolesi.ice_cream_api.repository.model;

import java.math.BigDecimal;

import com.jrpolesi.ice_cream_api.entities.IceCream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class IceCreamModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @Column(name = "flavor", nullable = false)
  String flavor;

  @Column(name = "size", nullable = false)
  String size;

  @Column(name = "price", nullable = false)
  BigDecimal price;

  public IceCreamModel() {
  }

  public IceCream toEntity() {
    return IceCream.of(id, flavor, size, price);
  }
}
