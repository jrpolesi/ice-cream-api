package com.jrpolesi.ice_cream_api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jrpolesi.ice_cream_api.dto.CreateIceCreamRequestDto;
import com.jrpolesi.ice_cream_api.dto.GetIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.entities.IceCream;
import com.jrpolesi.ice_cream_api.gateway.IConeGateway;
import com.jrpolesi.ice_cream_api.gateway.IIceCreamGateway;

@ExtendWith(MockitoExtension.class)
public class IceCreamServiceTest {

  @Mock
  private IIceCreamGateway iceCreamGateway;

  @Mock
  private IConeGateway coneGateway;

  @InjectMocks
  private IceCreamServiceImpl iceCreamService;

  @Test
  public void givenValidIceCreamDto_whenCallsCreateIceCream_thenReturnsCreatedIceCream() {
    // Given
    final var input = new CreateIceCreamRequestDto(
        "Chocolate",
        "L",
        new BigDecimal("5.50"),
        1);

    final var cone = Cone.with(input.coneId(), "Waffle", "L");
    final var expectedIceCream = IceCream.with(
        5,
        input.flavor(),
        input.size(),
        input.price(),
        cone);

    when(coneGateway.findById(input.coneId())).thenReturn(cone);

    when(iceCreamGateway.save(any())).thenReturn(expectedIceCream);

    // When
    final var output = iceCreamService.createIceCream(input);

    // Then
    assertNotNull(output);
    assertEquals(expectedIceCream.getId(), output.id());
    assertEquals(input.flavor(), output.flavor());
    assertEquals(input.size(), output.size());
    assertEquals(input.price(), output.price());
    assertEquals(input.coneId(), output.coneId());

    verify(coneGateway, times(1)).findById(input.coneId());

    verify(iceCreamGateway, times(1))
        .save(argThat(iceCream -> Objects.isNull(iceCream.getId())
            && Objects.equals(iceCream.getFlavor(), input.flavor())
            && Objects.equals(iceCream.getSize(), input.size())
            && Objects.equals(iceCream.getPrice(), input.price())
            && Objects.equals(iceCream.getCone(), cone)));
  }

  @Test
  public void givenValidId_whenCallsGetIceCreamById_thenReturnsIceCream() {
    // Given
    final var cone = Cone.with(1, "Waffle", "L");
    final var expectedIceCream = IceCream.with(
        1,
        "Chocolate",
        "L",
        new BigDecimal("5.50"),
        cone);

    when(iceCreamGateway.findById(expectedIceCream.getId())).thenReturn(expectedIceCream);

    // When
    final var output = iceCreamService.getIceCreamById(expectedIceCream.getId());

    // Then
    assertIceCreamEquals(expectedIceCream, output);

    verify(iceCreamGateway, times(1)).findById(expectedIceCream.getId());
  }

  @Test
  public void givenValidSizeQuery_whenCallsGetAllIceCreams_thenReturnsIceCreams() {
    // Given
    final var cone = Cone.with(1, "Waffle", "L");
    final var expectedIceCream = IceCream.with(
        1,
        "Strawberry",
        "X",
        new BigDecimal("5.80"),
        cone);

    when(iceCreamGateway.findAll(expectedIceCream.getSize())).thenReturn(List.of(expectedIceCream));

    // When
    final var output = iceCreamService.getAllIceCreams(expectedIceCream.getSize());

    // Then
    assertNotNull(output);
    assertEquals(1, output.size());

    final var foundIceCream = output.get(0);
    assertIceCreamEquals(expectedIceCream, foundIceCream);

    verify(iceCreamGateway, times(1)).findAll(expectedIceCream.getSize());
  }

  @Test
  public void givenSavedIceCreams_whenCallsGetAllIceCreams_thenReturnsAllIceCreams() {
    // Given;
    final var expectedIceCream1 = IceCream.with(
        1,
        "Chocolate",
        "L",
        new BigDecimal("5.50"),
        Cone.with(1, "Waffle", "L"));
    final var expectedIceCream2 = IceCream.with(
        2,
        "Vanilla",
        "M",
        new BigDecimal("4.00"),
        Cone.with(2, "Sugar", "M"));

    when(iceCreamGateway.findAll(null)).thenReturn(List.of(expectedIceCream1, expectedIceCream2));

    // When
    final var output = iceCreamService.getAllIceCreams(null);

    // Then
    assertNotNull(output);
    assertEquals(2, output.size());

    assertIceCreamEquals(expectedIceCream1, output.get(0));
    assertIceCreamEquals(expectedIceCream2, output.get(1));

    verify(iceCreamGateway, times(1)).findAll(null);
  }

  @Test
  public void givenEmptyIceCreams_whenCallsGetAllIceCreams_thenReturnsEmptyList() {
    // Given
    when(iceCreamGateway.findAll(null)).thenReturn(List.of());

    // When
    final var output = iceCreamService.getAllIceCreams(null);

    // Then
    assertNotNull(output);
    assertEquals(0, output.size());

    verify(iceCreamGateway, times(1)).findAll(null);
  }

  private void assertIceCreamEquals(IceCream expected, GetIceCreamResponseDto actual) {
    assertNotNull(actual);
    assertEquals(expected.getId(), actual.id());
    assertEquals(expected.getFlavor(), actual.flavor());
    assertEquals(expected.getSize(), actual.size());
    assertEquals(expected.getPrice(), actual.price());
    assertEquals(expected.getCone(), actual.cone());
  }
}
