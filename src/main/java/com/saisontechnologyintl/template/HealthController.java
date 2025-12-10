/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("/custom-health")
@Tag(name = "Health", description = "Health check operations")
public class HealthController {

  private final Counter healthCheckCounter;

  public HealthController(MeterRegistry meterRegistry) {
    this.healthCheckCounter = Counter.builder("health.checks").register(meterRegistry);
  }

  @Get
  @Operation(summary = "Health check", description = "Returns the health status of the service")
  @ApiResponse(responseCode = "200", description = "Service is healthy")
  public String health() {
    healthCheckCounter.increment();
    log.info("Health check requested - service is healthy");
    log.debug("Health check counter incremented, metrics sent to CloudWatch");
    return "OK";
  }
}

  // Test comment to trigger GitHub Actions CI pipeline
