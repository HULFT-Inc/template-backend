/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.command;

import com.saisontechnologyintl.template.strategy.AwsOperationStrategy;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S3ListBucketsCommand implements AwsCommand {

  private static final Logger LOG = LoggerFactory.getLogger(S3ListBucketsCommand.class);
  private final AwsOperationStrategy strategy;

  public S3ListBucketsCommand(AwsOperationStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public Map<String, Object> execute() {
    LOG.info("Executing S3 list buckets command");
    return strategy.execute();
  }

  @Override
  public void undo() {
    LOG.info("S3 list buckets command cannot be undone");
  }
}
