/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import com.saisontechnologyintl.template.entity.User;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@MicronautTest
@Testcontainers
class UserControllerTest implements TestPropertyProvider {

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:15-alpine")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

  @Inject RequestSpecification spec;

  @Override
  public Map<String, String> getProperties() {
    return Map.of(
        "datasources.default.url", postgres.getJdbcUrl(),
        "datasources.default.username", postgres.getUsername(),
        "datasources.default.password", postgres.getPassword(),
        "jpa.default.properties.hibernate.hbm2ddl.auto", "create-drop");
  }

  @Test
  @DisplayName("Should create and retrieve user")
  void shouldCreateAndRetrieveUser() {
    User user = User.builder().firstName("Test").lastName("User").email("test@example.com").build();

    // Create user
    Integer userId =
        given(spec)
            .contentType("application/json")
            .body(user)
            .when()
            .post("/users")
            .then()
            .statusCode(201)
            .body("firstName", equalTo("Test"))
            .body("lastName", equalTo("User"))
            .body("email", equalTo("test@example.com"))
            .body("id", notNullValue())
            .extract()
            .path("id");

    // Retrieve user
    given(spec)
        .when()
        .get("/users/" + userId)
        .then()
        .statusCode(200)
        .body("firstName", equalTo("Test"))
        .body("email", equalTo("test@example.com"));
  }

  @Test
  @DisplayName("Should get user statistics")
  void shouldGetUserStatistics() {
    given(spec)
        .when()
        .get("/users/stats")
        .then()
        .statusCode(200)
        .body("totalUsers", greaterThan(-1))
        .body("activeUsers", greaterThan(-1));
  }

  @Test
  @DisplayName("Should return 404 for non-existent user")
  void shouldReturn404ForNonExistentUser() {
    given(spec).when().get("/users/99999").then().statusCode(404);
  }
}
