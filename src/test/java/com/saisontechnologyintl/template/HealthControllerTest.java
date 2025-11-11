/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@MicronautTest
class HealthControllerTest {

  @Mock private MeterRegistry meterRegistry;

  @Mock private Counter healthCheckCounter;

  private HealthController healthController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    healthController = new HealthController(meterRegistry);
  }

  @Test
  @DisplayName("Health endpoint should return OK")
  void healthShouldReturnOk() {
    // When
    String result = healthController.health();

    // Then
    assertThat(result).isEqualTo("OK");
  }
}
