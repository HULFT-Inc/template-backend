/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.deployer.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.vpclattice.VpcLatticeClient;
import software.amazon.awssdk.services.vpclattice.model.CreateServiceNetworkRequest;
import software.amazon.awssdk.services.vpclattice.model.CreateServiceRequest;
import software.amazon.awssdk.services.vpclattice.model.CreateTargetGroupRequest;
import software.amazon.awssdk.services.vpclattice.model.TargetGroupConfig;
import software.amazon.awssdk.services.vpclattice.model.TargetGroupType;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class LatticeService {

  @Inject private final VpcLatticeClient latticeClient;

  public Map<String, Object> createServiceNetwork(String name, String vpcId) {
    log.info("Creating VPC Lattice service network: {}", name);

    try {
      var response =
          latticeClient.createServiceNetwork(
              CreateServiceNetworkRequest.builder()
                  .name(name)
                  .tags(Map.of("Environment", "predev", "Project", "template-backend"))
                  .build());

      Map<String, Object> result = new HashMap<>();
      result.put("serviceNetworkId", response.id());
      result.put("serviceNetworkArn", response.arn());
      result.put("name", response.name());
      result.put("status", "ACTIVE");

      log.info("Service network created: {}", response.id());
      return result;
    } catch (Exception e) {
      log.error("Failed to create service network", e);
      throw new RuntimeException("Service network creation failed", e);
    }
  }

  public Map<String, Object> createService(String name, String serviceNetworkId) {
    log.info("Creating VPC Lattice service: {}", name);

    try {
      var response =
          latticeClient.createService(
              CreateServiceRequest.builder()
                  .name(name)
                  .tags(Map.of("Environment", "predev", "Project", "template-backend"))
                  .build());

      Map<String, Object> result = new HashMap<>();
      result.put("serviceId", response.id());
      result.put("serviceArn", response.arn());
      result.put("name", response.name());
      result.put("status", "ACTIVE");

      log.info("Service created: {}", response.id());
      return result;
    } catch (Exception e) {
      log.error("Failed to create service", e);
      throw new RuntimeException("Service creation failed", e);
    }
  }

  public Map<String, Object> createTargetGroup(String name, String vpcId) {
    log.info("Creating VPC Lattice target group: {}", name);

    try {
      var response =
          latticeClient.createTargetGroup(
              CreateTargetGroupRequest.builder()
                  .name(name)
                  .type(TargetGroupType.IP)
                  .config(
                      TargetGroupConfig.builder()
                          .port(8080)
                          .protocol("HTTP")
                          .vpcIdentifier(vpcId)
                          .build())
                  .tags(Map.of("Environment", "predev", "Project", "template-backend"))
                  .build());

      Map<String, Object> result = new HashMap<>();
      result.put("targetGroupId", response.id());
      result.put("targetGroupArn", response.arn());
      result.put("name", response.name());
      result.put("status", "ACTIVE");

      log.info("Target group created: {}", response.id());
      return result;
    } catch (Exception e) {
      log.error("Failed to create target group", e);
      throw new RuntimeException("Target group creation failed", e);
    }
  }
}
