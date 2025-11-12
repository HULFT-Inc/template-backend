/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.factory;

import java.net.URI;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

public class AwsClientFactory {

  private final String region;
  private final Boolean localstackEnabled;
  private final String localstackEndpoint;
  private final ClientOverrideConfiguration clientConfig;

  public AwsClientFactory(
      String region,
      Boolean localstackEnabled,
      String localstackEndpoint,
      ClientOverrideConfiguration clientConfig) {
    this.region = region;
    this.localstackEnabled = localstackEnabled;
    this.localstackEndpoint = localstackEndpoint;
    this.clientConfig = clientConfig;
  }

  public S3Client createS3Client() {
    var builder =
        S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .overrideConfiguration(clientConfig);

    if (Boolean.TRUE.equals(localstackEnabled)) {
      builder.endpointOverride(URI.create(localstackEndpoint));
    }

    return builder.build();
  }

  public DynamoDbClient createDynamoDbClient() {
    var builder =
        DynamoDbClient.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .overrideConfiguration(clientConfig);

    if (Boolean.TRUE.equals(localstackEnabled)) {
      builder.endpointOverride(URI.create(localstackEndpoint));
    }

    return builder.build();
  }

  public SqsClient createSqsClient() {
    var builder =
        SqsClient.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .overrideConfiguration(clientConfig);

    if (Boolean.TRUE.equals(localstackEnabled)) {
      builder.endpointOverride(URI.create(localstackEndpoint));
    }

    return builder.build();
  }

  public SnsClient createSnsClient() {
    var builder =
        SnsClient.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .overrideConfiguration(clientConfig);

    if (Boolean.TRUE.equals(localstackEnabled)) {
      builder.endpointOverride(URI.create(localstackEndpoint));
    }

    return builder.build();
  }
}
