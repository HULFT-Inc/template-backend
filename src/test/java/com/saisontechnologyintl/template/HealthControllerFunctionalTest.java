/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@MicronautTest
class HealthControllerFunctionalTest {

  @Inject EmbeddedApplication<?> application;

  @Inject RequestSpecification spec;

  @Test
  @DisplayName("Health endpoint should be accessible and return OK")
  void healthEndpointShouldReturnOk() {
    given(spec).when().get("/health").then().statusCode(200).body(equalTo("OK"));
  }

  @Test
  @DisplayName("Metrics test endpoint should be accessible")
  void metricsTestEndpointShouldBeAccessible() {
    given(spec)
        .when()
        .get("/metrics/test")
        .then()
        .statusCode(200)
        .body(equalTo("Metrics recorded and logged to CloudWatch"));
  }
}
