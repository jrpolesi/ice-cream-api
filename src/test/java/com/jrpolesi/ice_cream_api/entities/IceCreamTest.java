package com.jrpolesi.ice_cream_api.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class IceCreamTest {

  @Test
  public void givenValidParams_whenCallsCreate_thenCreatesIceCream() {
    // Given
    final var expectedFlavor = "Chocolate";
    final var expectedSize = "L";
    final var expectedPrice = BigDecimal.valueOf(5.99);

    final var expectedCone = Cone.create("cone", "M");

    // When
    final var iceCream = IceCream.create(expectedFlavor, expectedSize, expectedPrice, expectedCone);

    // Then
    assertNotNull(iceCream);

    assertNull(iceCream.getId());
    assertEquals(expectedFlavor, iceCream.getFlavor());
    assertEquals(expectedSize, iceCream.getSize());
    assertEquals(expectedPrice, iceCream.getPrice());

    final var actualCone = iceCream.getCone();
    assertNull(expectedCone.getId());
    assertEquals(expectedCone.getType(), actualCone.getType());
    assertEquals(expectedCone.getSize(), actualCone.getSize());
  }
}
