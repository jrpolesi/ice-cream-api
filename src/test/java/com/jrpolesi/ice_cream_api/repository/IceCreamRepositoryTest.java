package com.jrpolesi.ice_cream_api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.entities.IceCream;
import com.jrpolesi.ice_cream_api.repository.model.IceCreamModel;

@DataJpaTest
public class IceCreamRepositoryTest {

  @Autowired
  private IIceCreamRepository iceCreamRepository;

  @Test
  public void givenValidParams_whenCallsSave_thenSavesIceCream() {
    // Given
    final var expectedICeCream = IceCream.create(
        "Vanilla",
        "L",
        new BigDecimal("5.50"),
        Cone.create("Waffle", "L"));

    final var iceCreamModel = IceCreamModel.fromEntity(expectedICeCream);

    // When
    final var savedIceCream = iceCreamRepository.save(iceCreamModel).toEntity();

    // Then
    assertNotNull(savedIceCream);
    assertNotNull(savedIceCream.getId());
    assertEquals(expectedICeCream.getFlavor(), savedIceCream.getFlavor());
    assertEquals(expectedICeCream.getSize(), savedIceCream.getSize());
    assertEquals(expectedICeCream.getPrice(), savedIceCream.getPrice());

    final var expectedCone = expectedICeCream.getCone();
    final var savedCone = savedIceCream.getCone();
    assertNotNull(savedIceCream.getCone());
    assertEquals(expectedCone.getType(), savedCone.getType());
    assertEquals(expectedCone.getSize(), savedCone.getSize());
  }

  @Test
  public void givenNullFlavor_whenCallsSave_thenThrowsException() {
    // Given
    final var iceCream = IceCream.create(
        null,
        "L",
        new BigDecimal("5.50"),
        Cone.create("Waffle", "L"));

    final var iceCreamModel = IceCreamModel.fromEntity(iceCream);

    // When
    final var actualException = assertThrows(DataIntegrityViolationException.class, () -> {
      iceCreamRepository.save(iceCreamModel);
    });

    // Then
    assertNotNull(actualException);

    final var actualCause = assertInstanceOf(ConstraintViolationException.class, actualException.getCause());

    final var errorMessage = actualCause.getMessage();

    assertNotNull(errorMessage);
    assertTrue(errorMessage.toLowerCase().contains("null"));
    assertTrue(errorMessage.toLowerCase().contains("flavor"));
  }

  @Test
  public void givenNullSize_whenCallsSave_thenThrowsException() {
    // Given
    final var iceCream = IceCream.create(
        "Vanilla",
        null,
        new BigDecimal("5.50"),
        Cone.create("Waffle", "L"));
    final var iceCreamModel = IceCreamModel.fromEntity(iceCream);

    // When
    final var actualException = assertThrows(DataIntegrityViolationException.class, () -> {
      iceCreamRepository.save(iceCreamModel);
    });

    // Then
    assertNotNull(actualException);

    final var actualCause = assertInstanceOf(ConstraintViolationException.class, actualException.getCause());

    final var errorMessage = actualCause.getMessage();

    assertNotNull(errorMessage);
    assertTrue(errorMessage.toLowerCase().contains("null"));
    assertTrue(errorMessage.toLowerCase().contains("size"));
  }

  @Test
  public void givenInvalidSize_whenCallsSave_thenThrowsException() {
    // Given
    final var iceCream = IceCream.create("Vanilla",
        "XXL",
        new BigDecimal("5.50"),
        Cone.create("Waffle", "L"));

    final var iceCreamModel = IceCreamModel.fromEntity(iceCream);

    // When
    final var actualException = assertThrows(DataIntegrityViolationException.class, () -> {
      iceCreamRepository.save(iceCreamModel);
    });

    // Then
    assertNotNull(actualException);

    final var actualCause = assertInstanceOf(DataException.class, actualException.getCause());

    final var errorMessage = actualCause.getMessage();

    assertNotNull(errorMessage);
    assertTrue(errorMessage.toLowerCase().contains("value too long"));
    assertTrue(errorMessage.toLowerCase().contains("size"));
  }
}
