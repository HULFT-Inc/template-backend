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
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateVpcRequest;
import software.amazon.awssdk.services.ec2.model.CreateSubnetRequest;
import software.amazon.awssdk.services.ec2.model.CreateInternetGatewayRequest;
import software.amazon.awssdk.services.ec2.model.AttachInternetGatewayRequest;
import software.amazon.awssdk.services.ec2.model.CreateRouteTableRequest;
import software.amazon.awssdk.services.ec2.model.CreateRouteRequest;
import software.amazon.awssdk.services.ec2.model.AssociateRouteTableRequest;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.IpRange;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.TagSpecification;
import software.amazon.awssdk.services.ec2.model.ResourceType;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.ecr.model.CreateRepositoryRequest;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.CreateRoleRequest;
import software.amazon.awssdk.services.iam.model.AttachRolePolicyRequest;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.CreateLogGroupRequest;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class InfrastructureService {

  @Inject private final Ec2Client ec2Client;
  @Inject private final EcrClient ecrClient;
  @Inject private final IamClient iamClient;
  @Inject private final CloudWatchLogsClient logsClient;

  public Map<String, Object> setupCompleteInfrastructure() {
    log.info("Setting up complete infrastructure in Ohio (us-east-2)");
    
    Map<String, Object> infrastructure = new HashMap<>();
    
    try {
      // 1. Create VPC
      Map<String, Object> vpc = createVpc();
      infrastructure.put("vpc", vpc);
      String vpcId = vpc.get("vpcId").toString();
      
      // 2. Create Internet Gateway
      Map<String, Object> igw = createInternetGateway(vpcId);
      infrastructure.put("internetGateway", igw);
      
      // 3. Create Subnets
      Map<String, Object> subnets = createSubnets(vpcId);
      infrastructure.put("subnets", subnets);
      
      // 4. Create Route Table
      Map<String, Object> routeTable = createRouteTable(vpcId, igw.get("igwId").toString(), 
          (List<String>) subnets.get("subnetIds"));
      infrastructure.put("routeTable", routeTable);
      
      // 5. Create Security Group
      Map<String, Object> securityGroup = createSecurityGroup(vpcId);
      infrastructure.put("securityGroup", securityGroup);
      
      // 6. Create ECR Repository
      Map<String, Object> ecr = createEcrRepository();
      infrastructure.put("ecr", ecr);
      
      // 7. Create IAM Role
      Map<String, Object> iamRole = createEcsTaskRole();
      infrastructure.put("iamRole", iamRole);
      
      // 8. Create CloudWatch Log Group
      Map<String, Object> logGroup = createLogGroup();
      infrastructure.put("logGroup", logGroup);
      
      infrastructure.put("status", "SUCCESS");
      infrastructure.put("region", "us-east-2");
      infrastructure.put("message", "Complete infrastructure setup completed");
      
      log.info("Infrastructure setup completed successfully");
      return infrastructure;
      
    } catch (Exception e) {
      log.error("Infrastructure setup failed", e);
      infrastructure.put("status", "FAILED");
      infrastructure.put("error", e.getMessage());
      return infrastructure;
    }
  }

  private Map<String, Object> createVpc() {
    log.info("Creating VPC");
    
    var response = ec2Client.createVpc(CreateVpcRequest.builder()
        .cidrBlock("10.0.0.0/16")
        .tagSpecifications(TagSpecification.builder()
            .resourceType(ResourceType.VPC)
            .tags(
                Tag.builder().key("Name").value("template-backend-vpc").build(),
                Tag.builder().key("Environment").value("predev").build(),
                Tag.builder().key("Project").value("template-backend").build()
            ).build())
        .build());
    
    String vpcId = response.vpc().vpcId();
    
    // Enable DNS hostnames
    ec2Client.modifyVpcAttribute(builder -> builder
        .vpcId(vpcId)
        .enableDnsHostnames(builder2 -> builder2.value(true)));
    
    Map<String, Object> result = new HashMap<>();
    result.put("vpcId", vpcId);
    result.put("cidrBlock", "10.0.0.0/16");
    
    log.info("VPC created: {}", vpcId);
    return result;
  }

  private Map<String, Object> createInternetGateway(String vpcId) {
    log.info("Creating Internet Gateway");
    
    var response = ec2Client.createInternetGateway(CreateInternetGatewayRequest.builder()
        .tagSpecifications(TagSpecification.builder()
            .resourceType(ResourceType.INTERNET_GATEWAY)
            .tags(
                Tag.builder().key("Name").value("template-backend-igw").build(),
                Tag.builder().key("Environment").value("predev").build()
            ).build())
        .build());
    
    String igwId = response.internetGateway().internetGatewayId();
    
    // Attach to VPC
    ec2Client.attachInternetGateway(AttachInternetGatewayRequest.builder()
        .internetGatewayId(igwId)
        .vpcId(vpcId)
        .build());
    
    Map<String, Object> result = new HashMap<>();
    result.put("igwId", igwId);
    
    log.info("Internet Gateway created and attached: {}", igwId);
    return result;
  }

  private Map<String, Object> createSubnets(String vpcId) {
    log.info("Creating subnets");
    
    // Public subnet 1 (us-east-2a)
    var subnet1Response = ec2Client.createSubnet(CreateSubnetRequest.builder()
        .vpcId(vpcId)
        .cidrBlock("10.0.1.0/24")
        .availabilityZone("us-east-2a")
        .tagSpecifications(TagSpecification.builder()
            .resourceType(ResourceType.SUBNET)
            .tags(
                Tag.builder().key("Name").value("template-backend-public-1").build(),
                Tag.builder().key("Environment").value("predev").build(),
                Tag.builder().key("Type").value("Public").build()
            ).build())
        .build());
    
    // Public subnet 2 (us-east-2b)
    var subnet2Response = ec2Client.createSubnet(CreateSubnetRequest.builder()
        .vpcId(vpcId)
        .cidrBlock("10.0.2.0/24")
        .availabilityZone("us-east-2b")
        .tagSpecifications(TagSpecification.builder()
            .resourceType(ResourceType.SUBNET)
            .tags(
                Tag.builder().key("Name").value("template-backend-public-2").build(),
                Tag.builder().key("Environment").value("predev").build(),
                Tag.builder().key("Type").value("Public").build()
            ).build())
        .build());
    
    String subnet1Id = subnet1Response.subnet().subnetId();
    String subnet2Id = subnet2Response.subnet().subnetId();
    
    // Enable auto-assign public IP
    ec2Client.modifySubnetAttribute(builder -> builder
        .subnetId(subnet1Id)
        .mapPublicIpOnLaunch(builder2 -> builder2.value(true)));
    
    ec2Client.modifySubnetAttribute(builder -> builder
        .subnetId(subnet2Id)
        .mapPublicIpOnLaunch(builder2 -> builder2.value(true)));
    
    Map<String, Object> result = new HashMap<>();
    result.put("subnet1Id", subnet1Id);
    result.put("subnet2Id", subnet2Id);
    result.put("subnetIds", List.of(subnet1Id, subnet2Id));
    
    log.info("Subnets created: {}, {}", subnet1Id, subnet2Id);
    return result;
  }

  private Map<String, Object> createRouteTable(String vpcId, String igwId, List<String> subnetIds) {
    log.info("Creating route table");
    
    var response = ec2Client.createRouteTable(CreateRouteTableRequest.builder()
        .vpcId(vpcId)
        .tagSpecifications(TagSpecification.builder()
            .resourceType(ResourceType.ROUTE_TABLE)
            .tags(
                Tag.builder().key("Name").value("template-backend-public-rt").build(),
                Tag.builder().key("Environment").value("predev").build()
            ).build())
        .build());
    
    String routeTableId = response.routeTable().routeTableId();
    
    // Create route to internet gateway
    ec2Client.createRoute(CreateRouteRequest.builder()
        .routeTableId(routeTableId)
        .destinationCidrBlock("0.0.0.0/0")
        .gatewayId(igwId)
        .build());
    
    // Associate with subnets
    for (String subnetId : subnetIds) {
      ec2Client.associateRouteTable(AssociateRouteTableRequest.builder()
          .routeTableId(routeTableId)
          .subnetId(subnetId)
          .build());
    }
    
    Map<String, Object> result = new HashMap<>();
    result.put("routeTableId", routeTableId);
    
    log.info("Route table created and associated: {}", routeTableId);
    return result;
  }

  private Map<String, Object> createSecurityGroup(String vpcId) {
    log.info("Creating security group");
    
    var response = ec2Client.createSecurityGroup(CreateSecurityGroupRequest.builder()
        .groupName("template-backend-sg")
        .description("Security group for template backend")
        .vpcId(vpcId)
        .tagSpecifications(TagSpecification.builder()
            .resourceType(ResourceType.SECURITY_GROUP)
            .tags(
                Tag.builder().key("Name").value("template-backend-sg").build(),
                Tag.builder().key("Environment").value("predev").build()
            ).build())
        .build());
    
    String sgId = response.groupId();
    
    // Allow HTTP traffic
    ec2Client.authorizeSecurityGroupIngress(AuthorizeSecurityGroupIngressRequest.builder()
        .groupId(sgId)
        .ipPermissions(
            IpPermission.builder()
                .ipProtocol("tcp")
                .fromPort(8080)
                .toPort(8080)
                .ipRanges(IpRange.builder().cidrIp("0.0.0.0/0").build())
                .build(),
            IpPermission.builder()
                .ipProtocol("tcp")
                .fromPort(80)
                .toPort(80)
                .ipRanges(IpRange.builder().cidrIp("0.0.0.0/0").build())
                .build()
        )
        .build());
    
    Map<String, Object> result = new HashMap<>();
    result.put("securityGroupId", sgId);
    
    log.info("Security group created: {}", sgId);
    return result;
  }

  private Map<String, Object> createEcrRepository() {
    log.info("Creating ECR repository");
    
    try {
      var response = ecrClient.createRepository(CreateRepositoryRequest.builder()
          .repositoryName("template-backend")
          .tags(
              software.amazon.awssdk.services.ecr.model.Tag.builder()
                  .key("Environment").value("predev").build(),
              software.amazon.awssdk.services.ecr.model.Tag.builder()
                  .key("Project").value("template-backend").build()
          )
          .build());
      
      Map<String, Object> result = new HashMap<>();
      result.put("repositoryUri", response.repository().repositoryUri());
      result.put("repositoryName", response.repository().repositoryName());
      
      log.info("ECR repository created: {}", response.repository().repositoryUri());
      return result;
      
    } catch (Exception e) {
      if (e.getMessage().contains("already exists")) {
        log.info("ECR repository already exists");
        Map<String, Object> result = new HashMap<>();
        result.put("repositoryName", "template-backend");
        result.put("status", "EXISTS");
        return result;
      }
      throw e;
    }
  }

  private Map<String, Object> createEcsTaskRole() {
    log.info("Creating ECS task execution role");
    
    String trustPolicy = """
        {
          "Version": "2012-10-17",
          "Statement": [
            {
              "Effect": "Allow",
              "Principal": {
                "Service": "ecs-tasks.amazonaws.com"
              },
              "Action": "sts:AssumeRole"
            }
          ]
        }
        """;
    
    try {
      var response = iamClient.createRole(CreateRoleRequest.builder()
          .roleName("ecsTaskExecutionRole")
          .assumeRolePolicyDocument(trustPolicy)
          .description("ECS Task Execution Role for template-backend")
          .build());
      
      // Attach managed policy
      iamClient.attachRolePolicy(AttachRolePolicyRequest.builder()
          .roleName("ecsTaskExecutionRole")
          .policyArn("arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy")
          .build());
      
      Map<String, Object> result = new HashMap<>();
      result.put("roleArn", response.role().arn());
      result.put("roleName", response.role().roleName());
      
      log.info("ECS task execution role created: {}", response.role().arn());
      return result;
      
    } catch (Exception e) {
      if (e.getMessage().contains("already exists")) {
        log.info("ECS task execution role already exists");
        Map<String, Object> result = new HashMap<>();
        result.put("roleName", "ecsTaskExecutionRole");
        result.put("status", "EXISTS");
        return result;
      }
      throw e;
    }
  }

  private Map<String, Object> createLogGroup() {
    log.info("Creating CloudWatch log group");
    
    try {
      var response = logsClient.createLogGroup(CreateLogGroupRequest.builder()
          .logGroupName("/ecs/template-backend")
          .build());
      
      Map<String, Object> result = new HashMap<>();
      result.put("logGroupName", "/ecs/template-backend");
      
      log.info("CloudWatch log group created: /ecs/template-backend");
      return result;
      
    } catch (Exception e) {
      if (e.getMessage().contains("already exists")) {
        log.info("CloudWatch log group already exists");
        Map<String, Object> result = new HashMap<>();
        result.put("logGroupName", "/ecs/template-backend");
        result.put("status", "EXISTS");
        return result;
      }
      throw e;
    }
  }
}
