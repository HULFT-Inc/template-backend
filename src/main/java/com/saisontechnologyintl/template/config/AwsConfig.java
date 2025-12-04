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
import com.saisontechnologyintl.template.factory.AwsClientFactory;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
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

  @Value("${aws.xray.enabled:false}")
  private Boolean xrayEnabled;

  @Value("${aws.xray.daemon-address:127.0.0.1:2000}")
  private String xrayDaemonAddress;

  @PostConstruct
  public void initXRay() {
    if (xrayEnabled) {
      System.setProperty("com.amazonaws.xray.emitters.daemonAddress", xrayDaemonAddress);
      
      AWSXRayRecorderBuilder builder =
          AWSXRayRecorderBuilder.standard()
              .withPlugin(new EC2Plugin())
              .withPlugin(new ECSPlugin())
              .withSamplingStrategy(new LocalizedSamplingStrategy());

      AWSXRay.setGlobalRecorder(builder.build());
      System.out.println("X-Ray initialized with daemon at: " + xrayDaemonAddress);
    }
  }

  @Bean
  public AwsClientFactory awsClientFactory() {
    ClientOverrideConfiguration.Builder configBuilder = ClientOverrideConfiguration.builder();

    if (xrayEnabled) {
      configBuilder.addExecutionInterceptor(
          new com.amazonaws.xray.interceptors.TracingInterceptor());
    }

    return new AwsClientFactory(
        region, localstackEnabled, localstackEndpoint, configBuilder.build());
  }

  @Bean
  public S3Client s3Client(AwsClientFactory factory) {
    return factory.createS3Client();
  }

  @Bean
  public DynamoDbClient dynamoDbClient(AwsClientFactory factory) {
    return factory.createDynamoDbClient();
  }

  @Bean
  public SqsClient sqsClient(AwsClientFactory factory) {
    return factory.createSqsClient();
  }

  @Bean
  public SnsClient snsClient(AwsClientFactory factory) {
    return factory.createSnsClient();
  }
}
