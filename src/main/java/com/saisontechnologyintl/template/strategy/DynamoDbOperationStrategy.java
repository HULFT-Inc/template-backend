/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.strategy;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class DynamoDbOperationStrategy implements AwsOperationStrategy {

  private static final Logger LOG = LoggerFactory.getLogger(DynamoDbOperationStrategy.class);
  private final DynamoDbClient dynamoDbClient;

  public DynamoDbOperationStrategy(DynamoDbClient dynamoDbClient) {
    this.dynamoDbClient = dynamoDbClient;
  }

  @Override
  public Map<String, Object> execute() {
    LOG.info("Executing DynamoDB operation");
    try {
      Map<String, AttributeValue> item = new HashMap<>();
      item.put("id", AttributeValue.builder().s("test-" + System.currentTimeMillis()).build());
      item.put("message", AttributeValue.builder().s("Strategy pattern test").build());

      PutItemRequest request =
          PutItemRequest.builder().tableName("template-test").item(item).build();

      dynamoDbClient.putItem(request);

      Map<String, Object> result = new HashMap<>();
      result.put("service", "DynamoDB");
      result.put("status", "success");
      result.put("message", "Item created");
      return result;
    } catch (Exception e) {
      LOG.error("DynamoDB operation failed", e);
      Map<String, Object> result = new HashMap<>();
      result.put("service", "DynamoDB");
      result.put("error", e.getMessage());
      result.put("status", "error");
      return result;
    }
  }
}
