/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.example.template;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller("/health")
@Tag(name = "Health", description = "Health check operations")
public class HealthController {

  @Get
  @Operation(summary = "Health check", description = "Returns the health status of the service")
  @ApiResponse(responseCode = "200", description = "Service is healthy")
  public String health() {
    return "OK";
  }
}
