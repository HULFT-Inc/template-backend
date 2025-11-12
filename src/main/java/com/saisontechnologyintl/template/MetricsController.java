/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("/metrics")
@Tag(name = "Metrics", description = "Custom metrics operations")
public class MetricsController {

  private final Counter requestCounter;
  private final Timer requestTimer;

  public MetricsController(MeterRegistry meterRegistry) {
    this.requestCounter = Counter.builder("custom.requests").register(meterRegistry);
    this.requestTimer = Timer.builder("custom.request.duration").register(meterRegistry);
  }

  @Get("/test")
  @Operation(summary = "Test metrics", description = "Generates custom metrics for testing")
  public String testMetrics() {
    try {
      return requestTimer.recordCallable(
          () -> {
            requestCounter.increment();
            log.info("Custom metrics generated - counter incremented");
            log.debug("Request timer recorded for metrics test endpoint");
            return "Metrics recorded and logged to CloudWatch";
          });
    } catch (Exception e) {
      log.error("Error recording metrics", e);
      return "Error recording metrics";
    }
  }
}
