/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudWatchMetricsObserver implements MetricsObserver {

  private static final Logger LOG = LoggerFactory.getLogger(CloudWatchMetricsObserver.class);

  @Override
  public void onMetricRecorded(String metricName, double value, String... tags) {
    LOG.info(
        "CloudWatch metric recorded: {} = {} with tags: {}",
        metricName,
        value,
        String.join(",", tags));
  }
}
