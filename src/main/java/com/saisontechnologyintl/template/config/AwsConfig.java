/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.config;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.ECSPlugin;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Factory
public class AwsConfig {

  @Value("${aws.region:us-east-1}")
  private String region;

  @Value("${aws.localstack.enabled:false}")
  private Boolean localstackEnabled;

  @Value("${aws.localstack.endpoint:http://localhost:4566}")
  private String localstackEndpoint;

  @PostConstruct
  public void initXRay() {
    AWSXRayRecorderBuilder builder =
        AWSXRayRecorderBuilder.standard()
            .withPlugin(new EC2Plugin())
            .withPlugin(new ECSPlugin())
            .withSamplingStrategy(new LocalizedSamplingStrategy());

    AWSXRay.setGlobalRecorder(builder.build());
  }

  @Bean
  public S3Client s3Client() {
    var builder =
        S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .overrideConfiguration(
                ClientOverrideConfiguration.builder()
                    .addExecutionInterceptor(
                        new com.amazonaws.xray.interceptors.TracingInterceptor())
                    .build());

    if (Boolean.TRUE.equals(localstackEnabled)) {
      builder.endpointOverride(URI.create(localstackEndpoint));
    }

    return builder.build();
  }

  @Bean
  public DynamoDbClient dynamoDbClient() {
    var builder =
        DynamoDbClient.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .overrideConfiguration(
                ClientOverrideConfiguration.builder()
                    .addExecutionInterceptor(
                        new com.amazonaws.xray.interceptors.TracingInterceptor())
                    .build());

    if (Boolean.TRUE.equals(localstackEnabled)) {
      builder.endpointOverride(URI.create(localstackEndpoint));
    }

    return builder.build();
  }

  @Bean
  public SqsClient sqsClient() {
    var builder =
        SqsClient.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .overrideConfiguration(
                ClientOverrideConfiguration.builder()
                    .addExecutionInterceptor(
                        new com.amazonaws.xray.interceptors.TracingInterceptor())
                    .build());

    if (Boolean.TRUE.equals(localstackEnabled)) {
      builder.endpointOverride(URI.create(localstackEndpoint));
    }

    return builder.build();
  }

  @Bean
  public SnsClient snsClient() {
    var builder =
        SnsClient.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .overrideConfiguration(
                ClientOverrideConfiguration.builder()
                    .addExecutionInterceptor(
                        new com.amazonaws.xray.interceptors.TracingInterceptor())
                    .build());

    if (Boolean.TRUE.equals(localstackEnabled)) {
      builder.endpointOverride(URI.create(localstackEndpoint));
    }

    return builder.build();
  }
}
