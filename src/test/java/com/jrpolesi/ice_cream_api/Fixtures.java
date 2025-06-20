package com.jrpolesi.ice_cream_api;

import java.math.BigDecimal;

import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.entities.IceCream;

public class Fixtures {
  public static Cone createCone(String type, String size) {
    return Cone.create(type, size);
  }

  public static Cone createCone() {
    return Fixtures.createCone("Waffle", "L");
  }

  public static IceCream createIceCream(String flavor, String size, BigDecimal price, Cone cone) {
    return IceCream.create(flavor, size, price, cone);
  }

  public static IceCream createIceCream(Cone cone) {
    return Fixtures.createIceCream(
        "Chocolate",
        "L",
        BigDecimal.valueOf(3.50),
        cone);
  }
}
