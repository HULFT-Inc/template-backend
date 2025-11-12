/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.deployer;

import com.saisontechnologyintl.deployer.service.EcsService;
import com.saisontechnologyintl.deployer.service.InfrastructureService;
import com.saisontechnologyintl.deployer.service.LatticeService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller("/deploy")
@RequiredArgsConstructor
@Slf4j
public class DeploymentController {

  @Inject private final InfrastructureService infrastructureService;
  @Inject private final LatticeService latticeService;
  @Inject private final EcsService ecsService;

  @Post("/setup")
  public Map<String, Object> setupInfrastructure() {
    log.info("Setting up complete infrastructure in Ohio (us-east-2)");
    return infrastructureService.setupCompleteInfrastructure();
  }

  @Post("/predev")
  public Map<String, Object> deployToPredev(
      @QueryValue Optional<String> vpcId,
      @QueryValue Optional<String> imageUri,
      @QueryValue Optional<List<String>> subnets) {

    log.info("Starting rapid deployment to predev environment in Ohio");

    Map<String, Object> deployment = new HashMap<>();
    deployment.put("environment", "predev");
    deployment.put("region", "us-east-2");
    deployment.put("timestamp", System.currentTimeMillis());

    try {
      // Setup infrastructure if not provided
      Map<String, Object> infrastructure = null;
      String actualVpcId;
      List<String> actualSubnets;
      String actualImageUri;

      if (vpcId.isEmpty() || subnets.isEmpty()) {
        log.info("Setting up infrastructure automatically");
        infrastructure = infrastructureService.setupCompleteInfrastructure();
        deployment.put("infrastructure", infrastructure);
        
        if (!"SUCCESS".equals(infrastructure.get("status"))) {
          deployment.put("status", "FAILED");
          deployment.put("error", "Infrastructure setup failed");
          return deployment;
        }
        
        
        Map<String, Object> vpc = (Map<String, Object>) infrastructure.get("vpc");
        actualVpcId = vpc.get("vpcId").toString();
        
        
        Map<String, Object> subnetsMap = (Map<String, Object>) infrastructure.get("subnets");
        
        actualSubnets = (List<String>) subnetsMap.get("subnetIds");
        
        
        Map<String, Object> ecr = (Map<String, Object>) infrastructure.get("ecr");
        actualImageUri = ecr.get("repositoryUri") + ":latest";
      } else {
        actualVpcId = vpcId.get();
        actualSubnets = subnets.get();
        actualImageUri = imageUri.orElse("template-backend:latest");
      }

      deployment.put("vpcId", actualVpcId);
      deployment.put("subnets", actualSubnets);
      deployment.put("imageUri", actualImageUri);

      // 1. Create VPC Lattice Service Network
      Map<String, Object> serviceNetwork =
          latticeService.createServiceNetwork("template-backend-predev", actualVpcId);
      deployment.put("serviceNetwork", serviceNetwork);

      // 2. Create VPC Lattice Service
      Map<String, Object> service =
          latticeService.createService(
              "template-backend-service", serviceNetwork.get("serviceNetworkId").toString());
      deployment.put("latticeService", service);

      // 3. Create Target Group
      Map<String, Object> targetGroup =
          latticeService.createTargetGroup("template-backend-targets", actualVpcId);
      deployment.put("targetGroup", targetGroup);

      // 4. Create ECS Cluster
      Map<String, Object> cluster = ecsService.createCluster("template-backend-predev");
      deployment.put("ecsCluster", cluster);

      // 5. Register Task Definition
      Map<String, Object> taskDef =
          ecsService.registerTaskDefinition("template-backend", actualImageUri);
      deployment.put("taskDefinition", taskDef);

      // 6. Create ECS Service
      Map<String, Object> ecsServiceResult =
          ecsService.createService(
              "template-backend-service",
              cluster.get("clusterArn").toString(),
              taskDef.get("taskDefinitionArn").toString(),
              actualSubnets);
      deployment.put("ecsService", ecsServiceResult);

      deployment.put("status", "SUCCESS");
      deployment.put("message", "Deployment completed successfully in Ohio (us-east-2)");

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
    status.put("region", "us-east-2");
    status.put("message", "AWS VPC Lattice Deployer for Ohio region");
    return status;
  }
}
