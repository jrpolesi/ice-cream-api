package com.jrpolesi.ice_cream_api.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jrpolesi.ice_cream_api.configurations.ConeConfigurations;
import com.jrpolesi.ice_cream_api.dto.GetConeResponseDto;
import com.jrpolesi.ice_cream_api.service.IConeService;

@WebMvcTest(ConeController.class)
@Import(ConeConfigurations.class)
@TestPropertySource(properties = "cone.search.enabled=false")
public class ConeControllerSearchDisabledTest {
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private IConeService coneService;

  @Test
  public void givenConeSearchDisabledAndSize_whenGetAllCones_thenGetAllConesIsCalled() throws Exception {
    // given
    final var cone1 = new GetConeResponseDto(2, "Sugar", "L");

    when(coneService.getAllCones()).thenReturn(List.of(cone1));

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

    verify(coneService, times(1)).getAllCones();
  }
}
