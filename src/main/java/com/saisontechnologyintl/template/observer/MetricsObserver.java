/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.observer;

public interface MetricsObserver {
  void onMetricRecorded(String metricName, double value, String... tags);
}
