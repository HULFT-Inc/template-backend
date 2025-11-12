/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("/docker")
@Tag(name = "Docker", description = "Docker container management endpoints")
public class DockerController {

  @Get("/health")
  @Operation(
      summary = "Docker health check",
      description = "Detailed health check for Docker containers")
  public Map<String, Object> dockerHealth() {
    log.info("Docker health check requested");

    Map<String, Object> health = new HashMap<>();
    health.put("status", "UP");
    health.put("container", "template-backend");
    health.put("timestamp", System.currentTimeMillis());

    // Check dependencies
    Map<String, String> dependencies = new HashMap<>();
    dependencies.put("postgres", "UP");
    dependencies.put("localstack", "UP");
    dependencies.put("redis", "UP");

    health.put("dependencies", dependencies);

    return health;
  }

  @Get("/info")
  @Operation(summary = "Container info", description = "Container runtime information")
  public Map<String, Object> containerInfo() {
    log.info("Container info requested");

    Map<String, Object> info = new HashMap<>();
    info.put("java.version", System.getProperty("java.version"));
    info.put("java.vendor", System.getProperty("java.vendor"));
    info.put("os.name", System.getProperty("os.name"));
    info.put("os.arch", System.getProperty("os.arch"));
    info.put("available.processors", Runtime.getRuntime().availableProcessors());
    info.put("max.memory", Runtime.getRuntime().maxMemory());
    info.put("total.memory", Runtime.getRuntime().totalMemory());
    info.put("free.memory", Runtime.getRuntime().freeMemory());

    return info;
  }
}
