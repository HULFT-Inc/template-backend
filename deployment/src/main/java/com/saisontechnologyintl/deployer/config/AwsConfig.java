/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.deployer.config;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.servicediscovery.ServiceDiscoveryClient;
import software.amazon.awssdk.services.vpclattice.VpcLatticeClient;

@Factory
public class AwsConfig {

  @Value("${aws.region:us-east-1}")
  private String region;

  @Value("${aws.profile:predev}")
  private String profile;

  @Bean
  public VpcLatticeClient vpcLatticeClient() {
    return VpcLatticeClient.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }

  @Bean
  public EcsClient ecsClient() {
    return EcsClient.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }

  @Bean
  public EcrClient ecrClient() {
    return EcrClient.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }

  @Bean
  public Ec2Client ec2Client() {
    return Ec2Client.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }

  @Bean
  public IamClient iamClient() {
    return IamClient.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }

  @Bean
  public CloudWatchLogsClient logsClient() {
    return CloudWatchLogsClient.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }

  @Bean
  public ServiceDiscoveryClient serviceDiscoveryClient() {
    return ServiceDiscoveryClient.builder()
        .region(Region.of(region))
        .credentialsProvider(ProfileCredentialsProvider.create(profile))
        .build();
  }
}
