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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/health")
@Tag(name = "Health", description = "Health check operations")
public class HealthController {

  private static final Logger LOG = LoggerFactory.getLogger(HealthController.class);
  private final Counter healthCheckCounter;

  public HealthController(MeterRegistry meterRegistry) {
    this.healthCheckCounter = Counter.builder("health.checks").register(meterRegistry);
  }

  @Get
  @Operation(summary = "Health check", description = "Returns the health status of the service")
  @ApiResponse(responseCode = "200", description = "Service is healthy")
  public String health() {
    healthCheckCounter.increment();
    LOG.info("Health check requested");
    return "OK";
  }
}
