/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.deployer.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.ContainerDefinition;
import software.amazon.awssdk.services.ecs.model.CreateClusterRequest;
import software.amazon.awssdk.services.ecs.model.CreateServiceRequest;
import software.amazon.awssdk.services.ecs.model.LaunchType;
import software.amazon.awssdk.services.ecs.model.LogConfiguration;
import software.amazon.awssdk.services.ecs.model.LogDriver;
import software.amazon.awssdk.services.ecs.model.NetworkConfiguration;
import software.amazon.awssdk.services.ecs.model.NetworkMode;
import software.amazon.awssdk.services.ecs.model.PortMapping;
import software.amazon.awssdk.services.ecs.model.RegisterTaskDefinitionRequest;
import software.amazon.awssdk.services.ecs.model.Tag;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class EcsService {

  @Inject private final EcsClient ecsClient;

  public Map<String, Object> createCluster(String clusterName) {
    log.info("Creating ECS cluster: {}", clusterName);

    try {
      var response =
          ecsClient.createCluster(
              CreateClusterRequest.builder()
                  .clusterName(clusterName)
                  .tags(
                      Tag.builder().key("Environment").value("predev").build(),
                      Tag.builder().key("Project").value("template-backend").build())
                  .build());

      Map<String, Object> result = new HashMap<>();
      result.put("clusterArn", response.cluster().clusterArn());
      result.put("clusterName", response.cluster().clusterName());
      result.put("status", response.cluster().status());

      log.info("ECS cluster created: {}", response.cluster().clusterArn());
      return result;
    } catch (Exception e) {
      log.error("Failed to create ECS cluster", e);
      throw new RuntimeException("ECS cluster creation failed", e);
    }
  }

  public Map<String, Object> registerTaskDefinition(String family, String imageUri) {
    log.info("Registering ECS task definition: {}", family);

    try {
      var containerDef =
          ContainerDefinition.builder()
              .name("template-backend")
              .image(imageUri)
              .memory(512)
              .cpu(256)
              .essential(true)
              .portMappings(
                  PortMapping.builder().containerPort(8080).protocol("tcp").build())
              .logConfiguration(
                  LogConfiguration.builder()
                      .logDriver(LogDriver.AWSLOGS)
                      .options(
                          Map.of(
                              "awslogs-group", "/ecs/template-backend",
                              "awslogs-region", "us-east-2",
                              "awslogs-stream-prefix", "ecs"))
                      .build())
              .build();

      var response =
          ecsClient.registerTaskDefinition(
              RegisterTaskDefinitionRequest.builder()
                  .family(family)
                  .networkMode(NetworkMode.AWSVPC)
                  .requiresCompatibilities(software.amazon.awssdk.services.ecs.model.Compatibility.FARGATE)
                  .cpu("256")
                  .memory("512")
                  .containerDefinitions(containerDef)
                  .executionRoleArn("arn:aws:iam::" + System.getenv("AWS_ACCOUNT_ID") + ":role/ecsTaskExecutionRole")
                  .tags(
                      Tag.builder().key("Environment").value("predev").build(),
                      Tag.builder().key("Project").value("template-backend").build())
                  .build());

      Map<String, Object> result = new HashMap<>();
      result.put("taskDefinitionArn", response.taskDefinition().taskDefinitionArn());
      result.put("family", response.taskDefinition().family());
      result.put("revision", response.taskDefinition().revision());

      log.info("Task definition registered: {}", response.taskDefinition().taskDefinitionArn());
      return result;
    } catch (Exception e) {
      log.error("Failed to register task definition", e);
      throw new RuntimeException("Task definition registration failed", e);
    }
  }

  public Map<String, Object> createService(
      String serviceName, String clusterArn, String taskDefinitionArn, List<String> subnets) {
    log.info("Creating ECS service: {}", serviceName);

    try {
      var response =
          ecsClient.createService(
              CreateServiceRequest.builder()
                  .serviceName(serviceName)
                  .cluster(clusterArn)
                  .taskDefinition(taskDefinitionArn)
                  .desiredCount(1)
                  .launchType(LaunchType.FARGATE)
                  .networkConfiguration(
                      NetworkConfiguration.builder()
                          .awsvpcConfiguration(
                              builder ->
                                  builder
                                      .subnets(subnets)
                                      .assignPublicIp("ENABLED")
                                      .securityGroups("sg-default"))
                          .build())
                  .tags(
                      Tag.builder().key("Environment").value("predev").build(),
                      Tag.builder().key("Project").value("template-backend").build())
                  .build());

      Map<String, Object> result = new HashMap<>();
      result.put("serviceArn", response.service().serviceArn());
      result.put("serviceName", response.service().serviceName());
      result.put("status", response.service().status());

      log.info("ECS service created: {}", response.service().serviceArn());
      return result;
    } catch (Exception e) {
      log.error("Failed to create ECS service", e);
      throw new RuntimeException("ECS service creation failed", e);
    }
  }
}
