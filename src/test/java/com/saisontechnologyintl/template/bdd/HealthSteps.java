/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class HealthSteps {

  @Inject
  @Client("/")
  HttpClient client;

  private HttpResponse<String> response;

  @Given("the service is running")
  public void theServiceIsRunning() {
    // Service is running via @MicronautTest
  }

  @When("I request the health endpoint")
  public void iRequestTheHealthEndpoint() {
    response = client.toBlocking().exchange(HttpRequest.GET("/health"), String.class);
  }

  @Then("I should receive a healthy response")
  public void iShouldReceiveAHealthyResponse() {
    assertEquals(200, response.getStatus().getCode());
  }

  @Then("the response should be {string}")
  public void theResponseShouldBe(String expectedResponse) {
    assertEquals(expectedResponse, response.body());
  }
}
