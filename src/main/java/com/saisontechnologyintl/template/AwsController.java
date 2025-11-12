/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Controller("/aws")
@Tag(name = "AWS", description = "AWS SDK integration examples")
public class AwsController {

  @Inject private S3Client s3Client;
  @Inject private DynamoDbClient dynamoDbClient;
  @Inject private SqsClient sqsClient;
  @Inject private SnsClient snsClient;

  @Get("/s3/buckets")
  @Operation(summary = "List S3 buckets", description = "Lists all S3 buckets")
  public Map<String, Object> listS3Buckets() {
    log.info("Listing S3 buckets");
    try {
      ListBucketsResponse response = s3Client.listBuckets();
      Map<String, Object> result = new HashMap<>();
      result.put("buckets", response.buckets().size());
      result.put("status", "success");
      log.info("Found {} S3 buckets", response.buckets().size());
      return result;
    } catch (Exception e) {
      log.error("Error listing S3 buckets", e);
      Map<String, Object> result = new HashMap<>();
      result.put("error", e.getMessage());
      result.put("status", "error");
      return result;
    }
  }

  @Post("/dynamodb/test")
  @Operation(summary = "Test DynamoDB", description = "Creates a test item in DynamoDB")
  public Map<String, Object> testDynamoDB() {
    log.info("Testing DynamoDB connection");
    try {
      Map<String, AttributeValue> item = new HashMap<>();
      item.put("id", AttributeValue.builder().s("test-" + System.currentTimeMillis()).build());
      item.put("message", AttributeValue.builder().s("Hello from template service").build());
      item.put(
          "timestamp",
          AttributeValue.builder().n(String.valueOf(System.currentTimeMillis())).build());

      PutItemRequest request =
          PutItemRequest.builder().tableName("template-test").item(item).build();

      dynamoDbClient.putItem(request);

      Map<String, Object> result = new HashMap<>();
      result.put("status", "success");
      result.put("message", "Item created in DynamoDB");
      log.info("Successfully created item in DynamoDB");
      return result;
    } catch (Exception e) {
      log.error("Error testing DynamoDB", e);
      Map<String, Object> result = new HashMap<>();
      result.put("error", e.getMessage());
      result.put("status", "error");
      return result;
    }
  }

  @Post("/sqs/test")
  @Operation(summary = "Test SQS", description = "Sends a test message to SQS")
  public Map<String, Object> testSQS() {
    log.info("Testing SQS connection");
    try {
      SendMessageRequest request =
          SendMessageRequest.builder()
              .queueUrl("http://localhost:4566/000000000000/template-queue")
              .messageBody("Hello from template service at " + System.currentTimeMillis())
              .build();

      sqsClient.sendMessage(request);

      Map<String, Object> result = new HashMap<>();
      result.put("status", "success");
      result.put("message", "Message sent to SQS");
      log.info("Successfully sent message to SQS");
      return result;
    } catch (Exception e) {
      log.error("Error testing SQS", e);
      Map<String, Object> result = new HashMap<>();
      result.put("error", e.getMessage());
      result.put("status", "error");
      return result;
    }
  }

  @Post("/sns/test")
  @Operation(summary = "Test SNS", description = "Publishes a test message to SNS")
  public Map<String, Object> testSNS() {
    log.info("Testing SNS connection");
    try {
      PublishRequest request =
          PublishRequest.builder()
              .topicArn("arn:aws:sns:us-east-1:000000000000:template-topic")
              .message("Hello from template service at " + System.currentTimeMillis())
              .build();

      snsClient.publish(request);

      Map<String, Object> result = new HashMap<>();
      result.put("status", "success");
      result.put("message", "Message published to SNS");
      log.info("Successfully published message to SNS");
      return result;
    } catch (Exception e) {
      log.error("Error testing SNS", e);
      Map<String, Object> result = new HashMap<>();
      result.put("error", e.getMessage());
      result.put("status", "error");
      return result;
    }
  }
}
