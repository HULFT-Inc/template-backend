/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.observer;

import java.util.ArrayList;
import java.util.List;

public class MetricsSubject {

  private final List<MetricsObserver> observers = new ArrayList<>();

  public void addObserver(MetricsObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(MetricsObserver observer) {
    observers.remove(observer);
  }

  public void notifyObservers(String metricName, double value, String... tags) {
    for (MetricsObserver observer : observers) {
      observer.onMetricRecorded(metricName, value, tags);
    }
  }
}
