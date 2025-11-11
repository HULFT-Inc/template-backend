/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;

@MicronautTest
public class HealthStepDefinitions {

  @Inject EmbeddedApplication<?> application;

  @Inject RequestSpecification spec;

  private Response response;

  @Given("the service is running")
  public void theServiceIsRunning() {
    assertThat(application.isRunning()).isTrue();
  }

  @When("I request the health endpoint")
  public void iRequestTheHealthEndpoint() {
    response = given(spec).when().get("/health");
  }

  @When("I request the health endpoint multiple times")
  public void iRequestTheHealthEndpointMultipleTimes() {
    for (int i = 0; i < 3; i++) {
      given(spec).when().get("/health");
    }
    response = given(spec).when().get("/health");
  }

  @Then("I should receive an OK response")
  public void iShouldReceiveAnOkResponse() {
    assertThat(response.getBody().asString()).isEqualTo("OK");
  }

  @Then("the response status should be {int}")
  public void theResponseStatusShouldBe(int statusCode) {
    assertThat(response.getStatusCode()).isEqualTo(statusCode);
  }

  @Then("the health check counter should be incremented")
  public void theHealthCheckCounterShouldBeIncremented() {
    assertThat(response.getStatusCode()).isEqualTo(200);
  }
}
