package com.jrpolesi.ice_cream_api.gateway.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.jrpolesi.ice_cream_api.entities.IceCream;
import com.jrpolesi.ice_cream_api.gateway.Fixtures;
import com.jrpolesi.ice_cream_api.repository.IConeRepository;
import com.jrpolesi.ice_cream_api.repository.IIceCreamRepository;
import com.jrpolesi.ice_cream_api.repository.model.ConeModel;
import com.jrpolesi.ice_cream_api.repository.model.IceCreamModel;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class IceCreamGatewayTest {
    @Autowired
    private IIceCreamRepository iceCreamRepository;

    @Autowired
    private IConeRepository coneRepository;

    @Autowired
    private IceCreamGateway iceCreamGateway;

    @Test
    public void givenSavedIceCreams_whenCallsFindAll_thenReturnsIceCreams() {
        // Given
        final var cone = Fixtures.createCone();
        final var savedCone = coneRepository.saveAndFlush(ConeModel.fromEntity(cone));
        cone.setId(savedCone.getId());

        final var iceCream1 = Fixtures.createIceCream(cone);
        final var iceCream2 = Fixtures.createIceCream(
                "Vanilla",
                "M",
                BigDecimal.valueOf(2.50),
                cone);

        final var createIceCream1 = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream1));
        iceCream1.setId(createIceCream1.getId());

        final var createIceCream2 = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream2));
        iceCream2.setId(createIceCream2.getId());

        // When
        final var actualIceCreams = iceCreamGateway.findAll(null);

        // Then
        assertNotNull(actualIceCreams);
        assertEquals(2, actualIceCreams.size());

        assertIceCream(iceCream1, actualIceCreams.get(0));
        assertIceCream(iceCream2, actualIceCreams.get(1));
    }

    @Test
    public void givenSavedIceCreams_whenCallsFindAllWithSize_thenReturnsFilteredIceCreams() {
        // Given
        final var cone = Fixtures.createCone();
        final var savedCone = coneRepository.saveAndFlush(ConeModel.fromEntity(cone));
        cone.setId(savedCone.getId());

        final var iceCream1 = Fixtures.createIceCream(cone);
        final var iceCream2 = Fixtures.createIceCream(
                "Vanilla",
                "M",
                BigDecimal.valueOf(2.50),
                cone);

        final var createIceCream1 = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream1));
        iceCream1.setId(createIceCream1.getId());

        final var createIceCream2 = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream2));
        iceCream2.setId(createIceCream2.getId());

        // When
        final var actualIceCreams = iceCreamGateway.findAll("M");

        // Then
        assertNotNull(actualIceCreams);
        assertEquals(1, actualIceCreams.size());
        assertIceCream(iceCream2, actualIceCreams.get(0));
    }

    @Test
    public void givenNoIceCreams_whenCallsFindAll_thenReturnsEmptyList() {
        // When
        final var actualIceCreams = iceCreamGateway.findAll(null);

        // Then
        assertNotNull(actualIceCreams);
        assertEquals(0, actualIceCreams.size());
    }

    @Test
    public void givenIceCreamId_whenCallsFindById_thenReturnsIceCream() {
        // Given
        final var cone = Fixtures.createCone();
        final var savedCone = coneRepository.saveAndFlush(ConeModel.fromEntity(cone));
        cone.setId(savedCone.getId());

        final var iceCream = Fixtures.createIceCream(cone);
        final var savedIceCream = iceCreamRepository.saveAndFlush(IceCreamModel.fromEntity(iceCream));
        iceCream.setId(savedIceCream.getId());

        // When
        final var actualIceCream = iceCreamGateway.findById(iceCream.getId());

        // Then
        assertIceCream(iceCream, actualIceCream);
    }

    @Test
    public void givenNonExistentIceCreamId_whenCallsFindById_thenThrowsException() {
        // Given
        final var expectedMessage = "Ice cream not found with id: 999";
        final var nonExistentId = 999;

        // When
        final var actualException = assertThrows(IllegalArgumentException.class, () -> {
            iceCreamGateway.findById(nonExistentId);
        });

        // Then
        assertNotNull(actualException);
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void givenValidIceCream_whenCallsSave_thenReturnsSavedIceCream() {
        // Given
        final var cone = Fixtures.createCone();
        final var savedCone = coneRepository.saveAndFlush(ConeModel.fromEntity(cone));
        cone.setId(savedCone.getId());

        final var iceCream = Fixtures.createIceCream(cone);

        // When
        final var actualIceCream = iceCreamGateway.save(iceCream);
        iceCream.setId(actualIceCream.getId());

        // Then
        assertNotNull(actualIceCream.getId());
        assertIceCream(iceCream, actualIceCream);
    }

    public void assertIceCream(IceCream expected, IceCream actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFlavor(), actual.getFlavor());
        assertEquals(expected.getSize(), actual.getSize());
        assertEquals(expected.getPrice(), actual.getPrice());

        final var expectedCone = expected.getCone();
        final var actualCone = actual.getCone();

        assertNotNull(actualCone);
        assertEquals(expectedCone.getId(), actualCone.getId());
        assertEquals(expectedCone.getType(), actualCone.getType());
        assertEquals(expectedCone.getSize(), actualCone.getSize());
    }
}
