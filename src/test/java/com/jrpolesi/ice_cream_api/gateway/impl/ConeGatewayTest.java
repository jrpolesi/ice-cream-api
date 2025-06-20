package com.jrpolesi.ice_cream_api.gateway.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.jrpolesi.ice_cream_api.Fixtures;
import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.repository.IConeRepository;
import com.jrpolesi.ice_cream_api.repository.model.ConeModel;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class ConeGatewayTest {
  @Autowired
  private IConeRepository coneRepository;

  @Autowired
  private ConeGatewayImpl coneGateway;

  @Test
  public void givenSavedCones_whenCallsFindAll_thenReturnsCones() {
    // Given
    final var cone1 = Fixtures.createCone();
    final var cone2 = Fixtures.createCone("Chocolate", "M");

    final var savedCone1 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone1));
    cone1.setId(savedCone1.getId());

    final var savedCone2 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone2));
    cone2.setId(savedCone2.getId());

    // When
    final var actualCones = coneGateway.findAll();

    // Then
    assertNotNull(actualCones);
    assertEquals(2, actualCones.size());

    assertCone(cone1, actualCones.get(0));
    assertCone(cone2, actualCones.get(1));
  }

  @Test
  public void givenSavedCones_whenCallsFindAllBySize_thenReturnsFilteredCones() {
    // Given
    final var cone1 = Fixtures.createCone();
    final var cone2 = Fixtures.createCone("Chocolate", "M");

    final var savedCone1 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone1));
    cone1.setId(savedCone1.getId());

    final var savedCone2 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone2));
    cone2.setId(savedCone2.getId());

    // When
    final var actualCones = coneGateway.findAllBySize("M");

    // Then
    assertNotNull(actualCones);
    assertEquals(1, actualCones.size());
    assertCone(cone2, actualCones.get(0));
  }

  @Test
  public void givenNoCones_whenCallsFindAll_thenReturnsEmptyList() {
    // When
    final var actualCones = coneGateway.findAll();

    // Then
    assertNotNull(actualCones);
    assertEquals(0, actualCones.size());
  }

  @Test
  public void givenConeId_whenCallsFindById_thenReturnsCone() {
    // Given
    final var cone = Fixtures.createCone();
    final var savedCone = coneRepository.saveAndFlush(ConeModel.fromEntity(cone));
    cone.setId(savedCone.getId());

    // When
    final Cone actualCone = coneGateway.findById(cone.getId());

    // Then
    assertCone(cone, actualCone);
  }

  @Test
  public void givenNonExistentConeId_whenCallsFindById_thenThrowsException() {
    // Given
    final var expectedMessage = "Cone not found with id: 999";
    final var nonExistentId = 999;

    // When
    final var actualException = assertThrows(IllegalArgumentException.class, () -> {
      coneGateway.findById(nonExistentId);
    });

    // Then
    assertNotNull(actualException);
    assertEquals(expectedMessage, actualException.getMessage());
  }

  @Test
  public void givenValidCone_whenCallsSave_thenReturnsSavedCone() {
    // Given
    final var cone = Fixtures.createCone();

    // When
    final Cone actualCone = coneGateway.save(cone);
    cone.setId(actualCone.getId());

    // Then
    assertNotNull(actualCone.getId());
    assertCone(cone, actualCone);
  }

  private void assertCone(Cone expected, Cone actual) {
    assertNotNull(actual);
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getSize(), actual.getSize());
  }
}
