/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.deployer;

import com.saisontechnologyintl.deployer.service.EcsService;
import com.saisontechnologyintl.deployer.service.LatticeService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller("/deploy")
@RequiredArgsConstructor
@Slf4j
public class DeploymentController {

  @Inject private final LatticeService latticeService;
  @Inject private final EcsService ecsService;

  @Post("/predev")
  public Map<String, Object> deployToPredev(
      @QueryValue String vpcId,
      @QueryValue String imageUri,
      @QueryValue List<String> subnets) {

    log.info("Starting rapid deployment to predev environment");

    Map<String, Object> deployment = new HashMap<>();
    deployment.put("environment", "predev");
    deployment.put("timestamp", System.currentTimeMillis());

    try {
      // 1. Create VPC Lattice Service Network
      Map<String, Object> serviceNetwork =
          latticeService.createServiceNetwork("template-backend-predev", vpcId);
      deployment.put("serviceNetwork", serviceNetwork);

      // 2. Create VPC Lattice Service
      Map<String, Object> service =
          latticeService.createService(
              "template-backend-service", serviceNetwork.get("serviceNetworkId").toString());
      deployment.put("latticeService", service);

      // 3. Create Target Group
      Map<String, Object> targetGroup =
          latticeService.createTargetGroup("template-backend-targets", vpcId);
      deployment.put("targetGroup", targetGroup);

      // 4. Create ECS Cluster
      Map<String, Object> cluster = ecsService.createCluster("template-backend-predev");
      deployment.put("ecsCluster", cluster);

      // 5. Register Task Definition
      Map<String, Object> taskDef =
          ecsService.registerTaskDefinition("template-backend", imageUri);
      deployment.put("taskDefinition", taskDef);

      // 6. Create ECS Service
      Map<String, Object> ecsServiceResult =
          ecsService.createService(
              "template-backend-service",
              cluster.get("clusterArn").toString(),
              taskDef.get("taskDefinitionArn").toString(),
              subnets);
      deployment.put("ecsService", ecsServiceResult);

      deployment.put("status", "SUCCESS");
      deployment.put("message", "Deployment completed successfully");

      log.info("Rapid deployment to predev completed successfully");
      return deployment;

    } catch (Exception e) {
      log.error("Deployment failed", e);
      deployment.put("status", "FAILED");
      deployment.put("error", e.getMessage());
      return deployment;
    }
  }

  @Post("/status")
  public Map<String, Object> getDeploymentStatus() {
    Map<String, Object> status = new HashMap<>();
    status.put("deployer", "active");
    status.put("environment", "predev");
    status.put("profile", "predev");
    status.put("region", "us-east-1");
    return status;
  }
}
