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
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

public class S3OperationStrategy implements AwsOperationStrategy {

  private static final Logger LOG = LoggerFactory.getLogger(S3OperationStrategy.class);
  private final S3Client s3Client;

  public S3OperationStrategy(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  @Override
  public Map<String, Object> execute() {
    LOG.info("Executing S3 operation");
    try {
      ListBucketsResponse response = s3Client.listBuckets();
      Map<String, Object> result = new HashMap<>();
      result.put("service", "S3");
      result.put("buckets", response.buckets().size());
      result.put("status", "success");
      return result;
    } catch (Exception e) {
      LOG.error("S3 operation failed", e);
      Map<String, Object> result = new HashMap<>();
      result.put("service", "S3");
      result.put("error", e.getMessage());
      result.put("status", "error");
      return result;
    }
  }
}
