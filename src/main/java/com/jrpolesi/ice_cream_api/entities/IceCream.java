package com.jrpolesi.ice_cream_api.entities;

import java.math.BigDecimal;

public class IceCream {
  private Integer id;
  private String flavor;
  private String size;
  private BigDecimal price;
  private Cone cone;

  private IceCream(Integer id, String flavor, String size, BigDecimal price, Cone cone) {
    this.id = id;
    this.flavor = flavor;
    this.size = size;
    this.price = price;
    this.cone = cone;
  }

  public static IceCream with(String flavor, String size, BigDecimal price, Cone cone) {
    return new IceCream(null, flavor, size, price, cone);
  }

  public static IceCream of(Integer id, String flavor, String size, BigDecimal price, Cone cone) {
    return new IceCream(id, flavor, size, price, cone);
  }

  public Integer getId() {
    return id;
  }

  public String getFlavor() {
    return flavor;
  }

  public String getSize() {
    return size;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Cone getCone() {
    return cone;
  }
}
