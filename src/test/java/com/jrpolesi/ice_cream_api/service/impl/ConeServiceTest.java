package com.jrpolesi.ice_cream_api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jrpolesi.ice_cream_api.dto.CreateConeRequestDto;
import com.jrpolesi.ice_cream_api.dto.GetConeResponseDto;
import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.gateway.IConeGateway;

@ExtendWith(MockitoExtension.class)
public class ConeServiceTest {
  @Mock
  private IConeGateway coneGateway;

  @InjectMocks
  private ConeServiceImpl coneService;

  @Test
  public void givenValidConeDto_whenCallsCreateCone_thenReturnsCreatedCone() {
    // Given
    final var input = new CreateConeRequestDto(
        "Chocolate",
        "L");

    final var expectedCone = Cone.with(1, input.type(), input.size());
    when(coneGateway.save(any())).thenReturn(expectedCone);

    // When
    final var output = coneService.createCone(input);

    // Then
    assertNotNull(output);
    assertEquals(expectedCone.getId(), output.id());
    assertEquals(input.type(), output.type());
    assertEquals(input.size(), output.size());

    verify(coneGateway, times(1))
        .save(argThat(cone -> Objects.isNull(cone.getId())
            && Objects.equals(cone.getType(), input.type())
            && Objects.equals(cone.getSize(), input.size())));
  }

  @Test
  public void givenValidId_whenCallsGetConeById_thenReturnsCone() {
    // Given
    final var expectedCone = Cone.with(1, "Chocolate", "L");

    when(coneGateway.findById(expectedCone.getId())).thenReturn(expectedCone);

    // When
    final var output = coneService.getConeById(expectedCone.getId());

    // Then
    assertConeEquals(expectedCone, output);

    verify(coneGateway, times(1)).findById(expectedCone.getId());
  }

  @Test
  public void givenValidSizeQuery_whenCallsSearchAllConesBySize_thenReturnsCone() {
    // Given
    final var expectedSize = "L";
    final var expectedCone = Cone.with(1, "Chocolate", expectedSize);

    when(coneGateway.findAllBySize(expectedSize)).thenReturn(List.of(expectedCone));

    // When
    final var output = coneService.searchAllConesBySize(expectedSize);

    // Then
    assertNotNull(output);
    assertEquals(1, output.size());

    final var foundCone = output.get(0);
    assertConeEquals(expectedCone, foundCone);

    verify(coneGateway, times(1)).findAllBySize(expectedSize);
  }

  @Test
  public void givenSavedCones_WhenCallsGetAllCones_thenReturnsAllCones() {
    // Given
    final var expectedCone1 = Cone.with(1, "Chocolate", "L");
    final var expectedCone2 = Cone.with(2, "Vanilla", "M");

    when(coneGateway.findAll()).thenReturn(List.of(expectedCone1, expectedCone2));

    // When
    final var output = coneService.getAllCones();

    // Then
    assertNotNull(output);
    assertEquals(2, output.size());

    final var foundCone1 = output.get(0);
    assertConeEquals(expectedCone1, foundCone1);

    final var foundCone2 = output.get(1);
    assertConeEquals(expectedCone2, foundCone2);

    verify(coneGateway, times(1)).findAll();
  }

  @Test
  public void givenEmptyCones_whenCallsGetAllCones_thenReturnsEmptyList() {
    // Given
    when(coneGateway.findAll()).thenReturn(List.of());

    // When
    final var output = coneService.getAllCones();

    // Then
    assertNotNull(output);
    assertEquals(0, output.size());

    verify(coneGateway, times(1)).findAll();
  }

  private void assertConeEquals(Cone expected, GetConeResponseDto actual) {
    assertNotNull(actual);
    assertEquals(expected.getId(), actual.id());
    assertEquals(expected.getType(), actual.type());
    assertEquals(expected.getSize(), actual.size());
  }
}
