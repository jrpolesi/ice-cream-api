package com.jrpolesi.ice_cream_api.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrpolesi.ice_cream_api.dto.CreateIceCreamRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetIceCreamResponseDto;
import com.jrpolesi.ice_cream_api.entities.Cone;
import com.jrpolesi.ice_cream_api.service.IIceCreamService;

@WebMvcTest(IceCreamController.class)
public class IceCreamControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private IIceCreamService iceCreamService;

  @Test
  public void givenValidBody_whenPostCreateIceCream_thenReturnsIceCream() throws Exception {
    // given
    final var cone = Cone.with(1, "Waffle", "M");
    final var input = new CreateIceCreamRequestDto(
        "Vanilla",
        "M",
        new BigDecimal("5.00"),
        cone.getId());

    final var expectedOutput = new CreateIceCreamResponseDto(
        1,
        input.flavor(),
        input.size(),
        input.price(),
        input.coneId());

    when(iceCreamService.createIceCream(input)).thenReturn(expectedOutput);

    // when
    final var request = post("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(input));

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(expectedOutput.id()))
        .andExpect(jsonPath("$.flavor").value(expectedOutput.flavor()))
        .andExpect(jsonPath("$.size").value(expectedOutput.size()))
        .andExpect(jsonPath("$.price").value(expectedOutput.price().doubleValue()))
        .andExpect(jsonPath("$.cone_id").value(expectedOutput.coneId()));

    verify(iceCreamService, times(1)).createIceCream(eq(input));
  }

  @Test
  public void givenInvalidEmptyBody_whenPostCreateIceCream_thenReturnsBadRequest() throws Exception {
    // given
    final var input = new CreateIceCreamRequestDto(
        null,
        null,
        null,
        null);

    // when
    final var request = post("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(input));

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.coneId").value("cone_id is required"))
        .andExpect(jsonPath("$.flavor").value("must not be null"))
        .andExpect(jsonPath("$.size").value("must not be null"))
        .andExpect(jsonPath("$.price").value("must not be null"));

    verify(iceCreamService, times(0)).createIceCream(eq(input));
  }

  @Test
  public void givenInvalidBody_whenPostCreateIceCream_thenReturnsBadRequest() throws Exception {
    // given
    final var input = new CreateIceCreamRequestDto(
        "abchdgfhjsahdfiujsjdfudhnaudhjaudhjauduaudjauduncuaudfujdf",
        "XX",
        new BigDecimal("-5.00"),
        1);

    // when
    final var request = post("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(input));

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flavor")
            .value("flavor must be between 1 and 50 characters"))
        .andExpect(jsonPath("$.size").value("size must be a single character"))
        .andExpect(jsonPath("$.price").value("price must be a positive value"));

    verify(iceCreamService, times(0)).createIceCream(eq(input));
  }

  @Test
  public void givenIceCreamId_whenGetIceCreamById_thenReturnsIceCream() throws Exception {
    // given
    final var iceCreamId = 1;
    final var cone = Cone.with(1, "Waffle", "M");
    final var expectedOutput = new GetIceCreamResponseDto(
        iceCreamId,
        "Vanilla",
        "M",
        new BigDecimal("5.00"),
        cone);

    when(iceCreamService.getIceCreamById(iceCreamId)).thenReturn(expectedOutput);

    // when
    final var request = get("/ice-cream/" + iceCreamId)
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(expectedOutput.id()))
        .andExpect(jsonPath("$.flavor").value(expectedOutput.flavor()))
        .andExpect(jsonPath("$.size").value(expectedOutput.size()))
        .andExpect(jsonPath("$.price").value(expectedOutput.price().doubleValue()))
        .andExpect(jsonPath("$.cone.id").value(expectedOutput.cone().getId()))
        .andExpect(jsonPath("$.cone.type").value(expectedOutput.cone().getType()))
        .andExpect(jsonPath("$.cone.size").value(expectedOutput.cone().getSize()));

    verify(iceCreamService, times(1)).getIceCreamById(eq(iceCreamId));
  }

  @Test
  public void givenNonExistentIceCreamId_whenGetIceCreamById_thenReturnsNotFound() throws Exception {
    // given
    final var iceCreamId = 999;
    final var expectedMessage = "Ice cream not found with id: " + iceCreamId;

    when(iceCreamService.getIceCreamById(iceCreamId)).thenThrow(new IllegalArgumentException(expectedMessage));

    // when
    final var request = get("/ice-cream/" + iceCreamId)
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(expectedMessage))
        .andExpect(jsonPath("$.error").value("An unexpected error occurred"));

    verify(iceCreamService, times(1)).getIceCreamById(eq(iceCreamId));
  }

  @Test
  public void givenIceCreams_whenGetAllIceCreams_thenReturnsIceCreams() throws Exception {
    // given
    final var cone = Cone.with(1, "Waffle", "M");
    final var iceCream1 = new GetIceCreamResponseDto(
        1,
        "Vanilla",
        "M",
        new BigDecimal("5.00"),
        cone);
    final var iceCream2 = new GetIceCreamResponseDto(
        2,
        "Chocolate",
        "L",
        new BigDecimal("6.00"),
        cone);

    when(iceCreamService.getAllIceCreams(null)).thenReturn(List.of(iceCream1, iceCream2));

    // when
    final var request = get("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value(iceCream1.id()))
        .andExpect(jsonPath("$[0].flavor").value(iceCream1.flavor()))
        .andExpect(jsonPath("$[0].size").value(iceCream1.size()))
        .andExpect(jsonPath("$[0].price").value(iceCream1.price().doubleValue()))
        .andExpect(jsonPath("$[0].cone.id").value(iceCream1.cone().getId()))
        .andExpect(jsonPath("$[0].cone.type").value(iceCream1.cone().getType()))
        .andExpect(jsonPath("$[0].cone.size").value(iceCream1.cone().getSize()))
        .andExpect(jsonPath("$[1].id").value(iceCream2.id()))
        .andExpect(jsonPath("$[1].flavor").value(iceCream2.flavor()))
        .andExpect(jsonPath("$[1].size").value(iceCream2.size()))
        .andExpect(jsonPath("$[1].price").value(iceCream2.price().doubleValue()))
        .andExpect(jsonPath("$[1].cone.id").value(iceCream2.cone().getId()))
        .andExpect(jsonPath("$[1].cone.type").value(iceCream2.cone().getType()))
        .andExpect(jsonPath("$[1].cone.size").value(iceCream2.cone().getSize()));

    verify(iceCreamService, times(1)).getAllIceCreams(eq(null));
  }

  @Test
  public void givenIceCreamsWithSize_whenGetAllIceCreams_thenReturnsFilteredIceCreams() throws Exception {
    // given
    final var cone = Cone.with(1, "Waffle", "M");
    final var iceCream1 = new GetIceCreamResponseDto(
        1,
        "Vanilla",
        "M",
        new BigDecimal("5.00"),
        cone);

    when(iceCreamService.getAllIceCreams("M")).thenReturn(List.of(iceCream1));

    // when
    final var request = get("/ice-cream?size=M")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(iceCream1.id()))
        .andExpect(jsonPath("$[0].flavor").value(iceCream1.flavor()))
        .andExpect(jsonPath("$[0].size").value(iceCream1.size()))
        .andExpect(jsonPath("$[0].price").value(iceCream1.price().doubleValue()))
        .andExpect(jsonPath("$[0].cone.id").value(iceCream1.cone().getId()))
        .andExpect(jsonPath("$[0].cone.type").value(iceCream1.cone().getType()))
        .andExpect(jsonPath("$[0].cone.size").value(iceCream1.cone().getSize()));

    verify(iceCreamService, times(1)).getAllIceCreams(eq("M"));
  }

  @Test
  public void givenInvalidSize_whenGetAllIceCreams_thenReturnsBadRequest() throws Exception {
    // when
    final var request = get("/ice-cream?size=XX")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.['getIceCream.size']").value("Size param must be a single character"));

    verify(iceCreamService, times(0)).getAllIceCreams(eq("XX"));
  }

  @Test
  public void givenNoIceCreams_whenGetAllIceCreams_thenReturnsEmptyList() throws Exception {
    // then
    when(iceCreamService.getAllIceCreams(null)).thenReturn(List.of());

    // when
    final var request = get("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(0));

    verify(iceCreamService, times(1)).getAllIceCreams(eq(null));
  }
}
