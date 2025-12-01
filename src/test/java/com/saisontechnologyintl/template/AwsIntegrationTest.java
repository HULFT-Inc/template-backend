/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@MicronautTest
@Testcontainers
class AwsIntegrationTest implements TestPropertyProvider {

  @Container
  static LocalStackContainer localstack =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
          .withServices(Service.S3, Service.DYNAMODB, Service.SQS, Service.SNS);

  @Inject RequestSpecification spec;

  @Override
  public Map<String, String> getProperties() {
    return Map.of(
        "aws.localstack.enabled",
        "true",
        "aws.localstack.endpoint",
        localstack.getEndpointOverride(Service.S3).toString());
  }

  @Test
  @DisplayName("S3 integration should work with LocalStack")
  void s3IntegrationShouldWork() {
    given(spec)
        .when()
        .get("/aws/s3/buckets")
        .then()
        .statusCode(200)
        .body("status", equalTo("success"));
  }

  @Test
  @DisplayName("DynamoDB integration should work with LocalStack")
  void dynamoDbIntegrationShouldWork() {
    given(spec)
        .when()
        .post("/aws/dynamodb/test")
        .then()
        .statusCode(200)
        .body("status", equalTo("success"));
  }
}
