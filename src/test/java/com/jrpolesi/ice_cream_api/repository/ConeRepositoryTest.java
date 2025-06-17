package com.jrpolesi.ice_cream_api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.repository.model.ConeModel;

@DataJpaTest
public class ConeRepositoryTest {

  @Autowired
  private IConeRepository coneRepository;

  @Test
  public void givenValidParams_whenCallsSave_thenSavesCone() {
    // Given
    final var expectedCone = Cone.create("Chocolate", "L");
    final var coneModel = ConeModel.fromEntity(expectedCone);

    // When
    final var cone = coneRepository.save(coneModel).toEntity();

    // Then
    assertNotNull(cone);
    assertNotNull(cone.getId());
    assertEquals(expectedCone.getType(), cone.getType());
    assertEquals(expectedCone.getSize(), cone.getSize());
  }

  @Test
  public void givenNullType_whenCallsSave_thenThrowsException() {
    // Given
    final var expectedCone = Cone.create(null, "L");
    final var coneModel = ConeModel.fromEntity(expectedCone);

    // When
    final var actualException = assertThrows(DataIntegrityViolationException.class, () -> {
      coneRepository.save(coneModel);
    });

    // Then
    assertNotNull(actualException);

    final var actualCause = assertInstanceOf(ConstraintViolationException.class, actualException.getCause());

    final var errorMessage = actualCause.getMessage();
    assertNotNull(errorMessage);
    assertTrue(errorMessage.toLowerCase().contains("null"));
    assertTrue(errorMessage.toLowerCase().contains("type"));
  }

  @Test
  public void givenNullSize_whenCallsSave_thenThrowsException() {
    // Given
    final var expectedCone = Cone.create("Chocolate", null);
    final var coneModel = ConeModel.fromEntity(expectedCone);

    // When
    final var actualException = assertThrows(DataIntegrityViolationException.class, () -> {
      coneRepository.save(coneModel);
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
    final var expectedCone = Cone.create("Chocolate", "XXL");
    final var coneModel = ConeModel.fromEntity(expectedCone);

    // When
    final var actualException = assertThrows(DataIntegrityViolationException.class, () -> {
      coneRepository.save(coneModel);
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
