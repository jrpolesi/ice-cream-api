package com.jrpolesi.ice_cream_api.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ConeTest {

  @Test
  public void givenValidParams_whenCallsCreate_thenCreatesCone() {
    // Given
    final var expectedType = "Chocolate";
    final var expectedSize = "L";

    // When
    final var cone = Cone.create(expectedType, expectedSize);

    // Then
    assertNotNull(cone);

    assertNull(cone.getId());
    assertEquals(expectedType, cone.getType());
    assertEquals(expectedSize, cone.getSize());
  }
}
