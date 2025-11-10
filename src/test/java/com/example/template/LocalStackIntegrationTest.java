/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.example.template;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Testcontainers
class LocalStackIntegrationTest {

  @Container
  static LocalStackContainer localstack =
      new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.0"))
          .withServices(Service.DYNAMODB, Service.SQS, Service.SNS);

  @Test
  void shouldConnectToDynamoDB() {
    DynamoDbClient dynamoDb =
        DynamoDbClient.builder()
            .endpointOverride(localstack.getEndpointOverride(Service.DYNAMODB))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        localstack.getAccessKey(), localstack.getSecretKey())))
            .region(Region.of(localstack.getRegion()))
            .build();

    assertNotNull(dynamoDb.listTables());
  }

  @Test
  void shouldConnectToSQS() {
    SqsClient sqs =
        SqsClient.builder()
            .endpointOverride(localstack.getEndpointOverride(Service.SQS))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        localstack.getAccessKey(), localstack.getSecretKey())))
            .region(Region.of(localstack.getRegion()))
            .build();

    assertNotNull(sqs.listQueues());
  }

  @Test
  void shouldConnectToSNS() {
    SnsClient sns =
        SnsClient.builder()
            .endpointOverride(localstack.getEndpointOverride(Service.SNS))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        localstack.getAccessKey(), localstack.getSecretKey())))
            .region(Region.of(localstack.getRegion()))
            .build();

    assertNotNull(sns.listTopics());
  }
}
