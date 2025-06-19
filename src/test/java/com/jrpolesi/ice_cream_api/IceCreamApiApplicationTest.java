package com.jrpolesi.ice_cream_api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IceCreamApiApplicationTest {
  @Test
  void main_runsWithoutException() {
    assertDoesNotThrow(() -> {
      IceCreamApiApplication.main(new String[] {});
    });
  }
}
