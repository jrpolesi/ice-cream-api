package com.jrpolesi.ice_cream_api.repository.model;

import java.math.BigDecimal;

import com.jrpolesi.ice_cream_api.entities.IceCream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ice_creams")
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

  @ManyToOne(fetch = FetchType.LAZY)
  ConeModel cone;

  public IceCreamModel() {
  }

  private IceCreamModel(Integer id, String flavor, String size, BigDecimal price, ConeModel cone) {
    this.id = id;
    this.flavor = flavor;
    this.size = size;
    this.price = price;
    this.cone = cone;
  }

  public IceCream toEntity() {
    return IceCream.of(id, flavor, size, price, cone.toEntity());
  }

  public static IceCreamModel fromEntity(IceCream iceCream) {
    return new IceCreamModel(
        iceCream.getId(),
        iceCream.getFlavor(),
        iceCream.getSize(),
        iceCream.getPrice(),
        ConeModel.fromEntity(iceCream.getCone()));
  }
}
