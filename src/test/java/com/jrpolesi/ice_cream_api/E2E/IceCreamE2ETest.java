package com.jrpolesi.ice_cream_api.E2E;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;
import com.jrpolesi.ice_cream_api.Fixtures;
import com.jrpolesi.ice_cream_api.repository.IConeRepository;
import com.jrpolesi.ice_cream_api.repository.IIceCreamRepository;
import com.jrpolesi.ice_cream_api.repository.model.ConeModel;
import com.jrpolesi.ice_cream_api.repository.model.IceCreamModel;

@SpringBootTest
@AutoConfigureMockMvc
public class IceCreamE2ETest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private IIceCreamRepository iceCreamRepository;

  @Autowired
  private IConeRepository coneRepository;

  @BeforeEach
  public void cleanUp() {
    iceCreamRepository.deleteAll();
    coneRepository.deleteAll();
  }

  @Test
  public void givenValidBody_whenPostCreateIceCream_thenReturnsIceCream() throws Exception {
    // given
    final var cone = Fixtures.createCone();
    final var savedCone = coneRepository.saveAndFlush(ConeModel.fromEntity(cone));
    cone.setId(savedCone.getId());

    final var body = String.format("""
        {
            \"flavor\": \"Vanilla\",
            \"size\": \"M\",
            \"price\": 4.50,
            \"cone_id\": %d
        }
        """, cone.getId());

    // when
    final var request = post("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.flavor").value("Vanilla"))
        .andExpect(jsonPath("$.size").value("M"))
        .andExpect(jsonPath("$.price").value(4.50))
        .andExpect(jsonPath("$.cone_id").value(cone.getId()));

    final var responseBody = response.andReturn().getResponse().getContentAsString();
    final var createdId = JsonPath.read(responseBody, "$.id");

    final var savedIceCreams = iceCreamRepository.findAll();
    assertEquals(1, savedIceCreams.size());

    final var createdIceCream = savedIceCreams.get(0);
    assertEquals(createdId, createdIceCream.getId());
    assertEquals("Vanilla", createdIceCream.getFlavor());
    assertEquals("M", createdIceCream.getSize());
    assertEquals(new BigDecimal("4.50"), createdIceCream.getPrice());
    assertEquals(cone.getId(), createdIceCream.getCone().getId());
  }

  @Test
  public void givenInvalidEmptyBody_whenPostCreateIceCream_thenReturnsBadRequest() throws Exception {
    // given
    final var body = "{}";

    // when
    final var request = post("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.coneId").value("cone_id is required"))
        .andExpect(jsonPath("$.flavor").value("must not be null"))
        .andExpect(jsonPath("$.size").value("must not be null"))
        .andExpect(jsonPath("$.price").value("must not be null"));
  }

  @Test
  public void givenInvalidBody_whenPostCreateIceCream_thenReturnsBadRequest() throws Exception {
    // given
    final var body = """
        {
            "flavor": "abchdgfhjsahdfiujsjdfudhnaudhjaudhjauduaudjauduncuaudfujdf",
            "size": "XXL",
            "price": -5.00,
            "cone_id": 1
        }
        """;

    // when
    final var request = post("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flavor")
            .value("flavor must be between 1 and 50 characters"))
        .andExpect(jsonPath("$.size").value("size must be a single character"))
        .andExpect(jsonPath("$.price").value("price must be a positive value"));
  }

  @Test
  public void givenIceCreamId_whenCallsGetIceCreamById_thenReturnsIceCream() throws Exception {
    // given
    final var cone = Fixtures.createCone();
    final var savedCone = coneRepository.saveAndFlush(ConeModel.fromEntity(cone));
    cone.setId(savedCone.getId());

    final var iceCream = Fixtures.createIceCream(
        "Strawberry",
        "S",
        new BigDecimal("3.00"),
        cone);
    final var createdIceCream = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream));
    iceCream.setId(createdIceCream.getId());

    // when
    final var request = get("/ice-cream/" + iceCream.getId())
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(iceCream.getId()))
        .andExpect(jsonPath("$.flavor").value(iceCream.getFlavor()))
        .andExpect(jsonPath("$.size").value(iceCream.getSize()))
        .andExpect(jsonPath("$.price").value(iceCream.getPrice().doubleValue()))
        .andExpect(jsonPath("$.cone.id").value(cone.getId()))
        .andExpect(jsonPath("$.cone.type").value(cone.getType()))
        .andExpect(jsonPath("$.cone.size").value(cone.getSize()));
  }

  @Test
  public void givenNonExistentIceCreamId_whenGetIceCreamById_thenReturnsNotFound() throws Exception {
    // given
    final var iceCreamId = 9999;
    final var expectedMessage = String.format("Ice cream not found with id: %s", iceCreamId);

    // when
    final var request = get("/ice-cream/9999")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(expectedMessage))
        .andExpect(jsonPath("$.error").value("An unexpected error occurred"));
  }

  @Test
  public void givenIceCreams_whenGetAllIceCreams_thenReturnsIceCreams() throws Exception {
    // given
    final var cone1 = Fixtures.createCone("Waffle", "M");
    final var savedCone1 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone1));
    cone1.setId(savedCone1.getId());

    final var cone2 = Fixtures.createCone("Sugar", "L");
    final var savedCone2 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone2));
    cone2.setId(savedCone2.getId());

    final var iceCream1 = Fixtures.createIceCream(cone1);
    final var savedIceCream1 = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream1));
    iceCream1.setId(savedIceCream1.getId());

    final var iceCream2 = Fixtures.createIceCream("Vanilla", "L", new BigDecimal("5.00"), cone2);
    final var savedIceCream2 = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream2));
    iceCream2.setId(savedIceCream2.getId());

    // when
    final var request = get("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(iceCream1.getId()))
        .andExpect(jsonPath("$[0].flavor").value(iceCream1.getFlavor()))
        .andExpect(jsonPath("$[0].size").value(iceCream1.getSize()))
        .andExpect(jsonPath("$[0].price").value(iceCream1.getPrice().doubleValue()))
        .andExpect(jsonPath("$[0].cone.id").value(cone1.getId()))
        .andExpect(jsonPath("$[0].cone.type").value(cone1.getType()))
        .andExpect(jsonPath("$[0].cone.size").value(cone1.getSize()))
        .andExpect(jsonPath("$[1].id").value(iceCream2.getId()))
        .andExpect(jsonPath("$[1].flavor").value(iceCream2.getFlavor()))
        .andExpect(jsonPath("$[1].size").value(iceCream2.getSize()))
        .andExpect(jsonPath("$[1].price").value(iceCream2.getPrice().doubleValue()))
        .andExpect(jsonPath("$[1].cone.id").value(cone2.getId()))
        .andExpect(jsonPath("$[1].cone.type").value(cone2.getType()))
        .andExpect(jsonPath("$[1].cone.size").value(cone2.getSize()));
  }

  @Test
  public void givenIceCreamsWithSize_whenGetAllIceCreams_thenReturnsFilteredIceCreams() throws Exception {
    // given
    final var cone = Fixtures.createCone("Waffle", "M");
    final var savedCone = coneRepository.saveAndFlush(ConeModel.fromEntity(cone));
    cone.setId(savedCone.getId());

    final var iceCream1 = Fixtures.createIceCream("Chocolate", "M", new BigDecimal("4.00"), cone);
    final var savedIceCream1 = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream1));
    iceCream1.setId(savedIceCream1.getId());

    final var iceCream2 = Fixtures.createIceCream("Strawberry", "L", new BigDecimal("5.00"), cone);
    final var savedIceCream2 = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream2));
    iceCream2.setId(savedIceCream2.getId());

    // when
    final var request = get("/ice-cream?size=M")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(iceCream1.getId()))
        .andExpect(jsonPath("$[0].flavor").value(iceCream1.getFlavor()))
        .andExpect(jsonPath("$[0].size").value("M"))
        .andExpect(jsonPath("$[0].price").value(iceCream1.getPrice().doubleValue()))
        .andExpect(jsonPath("$[0].cone.id").value(cone.getId()))
        .andExpect(jsonPath("$[0].cone.type").value(cone.getType()))
        .andExpect(jsonPath("$[0].cone.size").value(cone.getSize()));
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
  }

  @Test
  public void givenNoIceCreams_whenGetAllIceCreams_thenReturnsEmptyList() throws Exception {
    // when
    final var request = get("/ice-cream")
        .contentType(MediaType.APPLICATION_JSON);

    final var response = mockMvc.perform(request);

    // then
    response.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(0));
  }
}
