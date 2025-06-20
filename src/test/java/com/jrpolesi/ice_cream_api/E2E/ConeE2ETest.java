package com.jrpolesi.ice_cream_api.E2E;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.jrpolesi.ice_cream_api.repository.model.ConeModel;

@SpringBootTest
@AutoConfigureMockMvc
public class ConeE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IConeRepository coneRepository;

    @BeforeEach
    public void cleanUp() {
        coneRepository.deleteAll();
    }

    @Test
    void givenValidBody_whenPostCreateCone_thenReturnsCone() throws Exception {
        // given
        final var body = """
                {
                "cone_type": "Waffle",
                "size": "M"
                }
                """;

        // when
        final var request = post("/cone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        final var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.cone_type").value("Waffle"))
                .andExpect(jsonPath("$.size").value("M"));

        final var responseBody = response.andReturn().getResponse().getContentAsString();
        final var createdId = JsonPath.read(responseBody, "$.id");

        final var savedCones = coneRepository.findAll();

        assertEquals(1, savedCones.size());

        final var createdCone = savedCones.get(0).toEntity();
        assertEquals(createdId, createdCone.getId());
        assertEquals("Waffle", createdCone.getType());
        assertEquals("M", createdCone.getSize());
    }

    @Test
    public void givenInvalidEmptyBody_whenPostCreateCone_thenReturnsBadRequest() throws Exception {
        // given
        final var body = "{}";

        // when
        final var request = post("/cone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        final var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("must not be null"))
                .andExpect(jsonPath("$.size").value("must not be null"));
    }

    @Test
    public void givenInvalidBody_whenPostCreateCone_thenReturnsBadRequest() throws Exception {
        // given
        final var body = """
                {
                "cone_type": "jsjdhaudhjakajsudhaudiuaahcvicmnisiaismciudiosjsifg",
                "size": "XXL"
                }
                """;

        // when
        final var request = post("/cone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        final var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("cone_type must be between 1 and 50 characters"))
                .andExpect(jsonPath("$.size").value("size must be a single character"));
    }

    @Test
    public void givenConeId_whenCallsGetConeById_thenReturnsCone() throws Exception {
        // given
        final var cone = Fixtures.createCone();

        final var createdCode = coneRepository.saveAndFlush(ConeModel.fromEntity(cone));
        cone.setId(createdCode.getId());

        // when
        final var request = get("/cone/" + cone.getId())
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cone.getId()))
                .andExpect(jsonPath("$.cone_type").value(cone.getType()))
                .andExpect(jsonPath("$.size").value(cone.getSize()));
    }

    @Test
    public void givenInvalidConeId_whenCallsGetConeById_thenReturnsNotFound() throws Exception {
        // when
        final var request = get("/cone/999")
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Cone not found with id: 999"))
                .andExpect(jsonPath("$.error").value("An unexpected error occurred"));
        ;
    }

    @Test
    public void givenCones_whenGetAllCones_thenReturnsCones() throws Exception {
        // given
        final var cone1 = Fixtures.createCone();
        final var cone2 = Fixtures.createCone("Sugar", "L");

        final var createdCone1 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone1));
        cone1.setId(createdCone1.getId());

        final var createdCone2 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone2));
        cone2.setId(createdCone2.getId());

        // when
        final var request = get("/cone")
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(cone1.getId()))
                .andExpect(jsonPath("$[0].cone_type").value(cone1.getType()))
                .andExpect(jsonPath("$[0].size").value(cone1.getSize()))

                .andExpect(jsonPath("$[1].id").value(cone2.getId()))
                .andExpect(jsonPath("$[1].cone_type").value(cone2.getType()))
                .andExpect(jsonPath("$[1].size").value(cone2.getSize()));
    }

    @Test
    public void givenConesWithSize_whenGetAllCones_thenReturnsFilteredCones() throws Exception {
        // given
        final var cone1 = Fixtures.createCone("Waffle", "M");
        final var cone2 = Fixtures.createCone("Sugar", "L");

        final var createdCone1 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone1));
        cone1.setId(createdCone1.getId());

        final var createdCone2 = coneRepository.saveAndFlush(ConeModel.fromEntity(cone2));
        cone2.setId(createdCone2.getId());

        // when
        final var request = get("/cone?size=M")
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(cone1.getId()))
                .andExpect(jsonPath("$[0].cone_type").value(cone1.getType()))
                .andExpect(jsonPath("$[0].size").value(cone1.getSize()));
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
    }

    @Test
    public void givenNoCones_whenGetAllCones_thenReturnsEmptyList() throws Exception {
        // when
        final var request = get("/cone")
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}
