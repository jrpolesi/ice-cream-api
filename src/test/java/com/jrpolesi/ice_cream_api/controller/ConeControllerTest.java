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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrpolesi.ice_cream_api.dto.CreateConeRequestDto;
import com.jrpolesi.ice_cream_api.dto.CreateConeResponseDto;
import com.jrpolesi.ice_cream_api.dto.GetConeResponseDto;
import com.jrpolesi.ice_cream_api.service.IConeService;

@WebMvcTest(ConeController.class)
@Import(com.jrpolesi.ice_cream_api.configurations.ConeConfigurations.class)
public class ConeControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private IConeService coneService;

  @Test
  public void givenValidBody_whenPostCreateCone_thenReturnsCone() throws Exception {
    // given
    final var input = new CreateConeRequestDto("Waffle", "M");
    final var expectedOutput = new CreateConeResponseDto(1, input.type(), input.size());

    when(coneService.createCone(input)).thenReturn(expectedOutput);

    // when
    final var request = post("/cone")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(input));

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(expectedOutput.id()))
        .andExpect(jsonPath("$.cone_type").value(expectedOutput.type()))
        .andExpect(jsonPath("$.size").value(expectedOutput.size()));

    verify(coneService, times(1)).createCone(eq(input));
  }

  @Test
  public void givenInvalidEmptyBody_whenPostCreateCone_thenReturnsBadRequest() throws Exception {
    // given
    final var input = new CreateConeRequestDto(null, null);

    // when
    final var request = post("/cone")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(input));

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.type").value("must not be null"))
        .andExpect(jsonPath("$.size").value("must not be null"));

    verify(coneService, times(0)).createCone(eq(input));
  }

  @Test
  public void givenInvalidBody_whenPostCreateCone_thenReturnsBadRequest() throws Exception {
    // given
    final var input = new CreateConeRequestDto(
        "jsjdhaudhjakajsudhaudiuaahcvicmnisiaismciudiosjsifg",
        "XX");

    // when
    final var request = post("/cone")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(input));

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.type").value("cone_type must be between 1 and 50 characters"))
        .andExpect(jsonPath("$.size").value("size must be a single character"));

    verify(coneService, times(0)).createCone(eq(input));
  }

  @Test
  public void givenConeId_whenCallsGetConeById_thenReturnsCone() throws Exception {
    // given
    final var cone = new GetConeResponseDto(1, "Waffle", "M");

    when(coneService.getConeById(1)).thenReturn(cone);

    // when
    final var request = get("/cone/1")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(cone.id()))
        .andExpect(jsonPath("$.cone_type").value(cone.type()))
        .andExpect(jsonPath("$.size").value(cone.size()));

    verify(coneService, times(1)).getConeById(eq(1));
  }

  @Test
  public void givenInvalidConeId_whenCallsGetConeById_thenReturnsNotFound() throws Exception {
    // given
    final var coneId = 999;
    final var expectedMessage = "Cone not found with id: " + coneId;

    when(coneService.getConeById(coneId)).thenThrow(new IllegalArgumentException(expectedMessage));

    // when
    final var request = get("/cone/" + coneId)
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(expectedMessage))
        .andExpect(jsonPath("$.error").value("An unexpected error occurred"));

    verify(coneService, times(1)).getConeById(eq(999));
  }

  @Test
  public void givenCones_whenGetAllCones_thenReturnsCones() throws Exception {
    // given
    final var cone1 = new GetConeResponseDto(1, "Waffle", "M");
    final var cone2 = new GetConeResponseDto(2, "Sugar", "L");

    when(coneService.getAllCones()).thenReturn(List.of(cone1, cone2));

    // when
    final var request = get("/cone")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value(cone1.id()))
        .andExpect(jsonPath("$[0].cone_type").value(cone1.type()))
        .andExpect(jsonPath("$[0].size").value(cone1.size()))
        .andExpect(jsonPath("$[1].id").value(cone2.id()))
        .andExpect(jsonPath("$[1].cone_type").value(cone2.type()))
        .andExpect(jsonPath("$[1].size").value(cone2.size()));

    verify(coneService, times(1)).getAllCones();
  }

  @Test
  public void givenConesWithSize_whenGetAllCones_thenReturnsFilteredCones() throws Exception {
    // given
    final var cone1 = new GetConeResponseDto(1, "Waffle", "M");

    when(coneService.searchAllConesBySize("M")).thenReturn(List.of(cone1));

    // when
    final var request = get("/cone?size=M")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(cone1.id()))
        .andExpect(jsonPath("$[0].cone_type").value(cone1.type()))
        .andExpect(jsonPath("$[0].size").value(cone1.size()));

    verify(coneService, times(1)).searchAllConesBySize(eq("M"));
  }

  @Test
  public void givenInvalidSize_whenGetAllCones_thenReturnsBadRequest() throws Exception {
    // when
    final var request = get("/cone?size=XX")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.['getCone.size']").value("Size param must be a single character"));

    verify(coneService, times(0)).searchAllConesBySize(eq("XX"));
  }

  @Test
  public void givenNoCones_whenGetAllCones_thenReturnsEmptyList() throws Exception {
    // given
    when(coneService.getAllCones()).thenReturn(List.of());

    // when
    final var request = get("/cone")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(0));

    verify(coneService, times(1)).getAllCones();
  }
}
