/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import com.saisontechnologyintl.template.command.AwsCommandInvoker;
import com.saisontechnologyintl.template.command.S3ListBucketsCommand;
import com.saisontechnologyintl.template.observer.CloudWatchMetricsObserver;
import com.saisontechnologyintl.template.observer.LogMetricsObserver;
import com.saisontechnologyintl.template.observer.MetricsSubject;
import com.saisontechnologyintl.template.singleton.ApplicationConfig;
import com.saisontechnologyintl.template.strategy.DynamoDbOperationStrategy;
import com.saisontechnologyintl.template.strategy.S3OperationStrategy;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;

@Controller("/patterns")
@Tag(name = "Design Patterns", description = "Gang of Four design patterns examples")
public class PatternController {

  private static final Logger LOG = LoggerFactory.getLogger(PatternController.class);

  @Inject private S3Client s3Client;
  @Inject private DynamoDbClient dynamoDbClient;

  private final MetricsSubject metricsSubject;
  private final AwsCommandInvoker commandInvoker;

  public PatternController() {
    this.metricsSubject = new MetricsSubject();
    this.metricsSubject.addObserver(new CloudWatchMetricsObserver());
    this.metricsSubject.addObserver(new LogMetricsObserver());
    this.commandInvoker = new AwsCommandInvoker();
  }

  @Get("/singleton")
  @Operation(
      summary = "Singleton pattern",
      description = "Demonstrates Singleton pattern with application config")
  public Map<String, Object> singletonExample() {
    LOG.info("Demonstrating Singleton pattern");

    ApplicationConfig config = ApplicationConfig.getInstance();

    Map<String, Object> result = new HashMap<>();
    result.put("pattern", "Singleton");
    result.put("appName", config.getProperty("app.name"));
    result.put("appVersion", config.getProperty("app.version"));
    result.put("metricsEnabled", config.getProperty("metrics.enabled"));

    metricsSubject.notifyObservers("singleton.access", 1.0, "pattern=singleton");

    return result;
  }

  @Post("/strategy")
  @Operation(
      summary = "Strategy pattern",
      description = "Demonstrates Strategy pattern with AWS operations")
  public Map<String, Object> strategyExample() {
    LOG.info("Demonstrating Strategy pattern");

    var s3Strategy = new S3OperationStrategy(s3Client);
    var dynamoStrategy = new DynamoDbOperationStrategy(dynamoDbClient);

    Map<String, Object> result = new HashMap<>();
    result.put("pattern", "Strategy");
    result.put("s3Result", s3Strategy.execute());
    result.put("dynamoResult", dynamoStrategy.execute());

    metricsSubject.notifyObservers("strategy.execution", 1.0, "pattern=strategy");

    return result;
  }

  @Post("/command")
  @Operation(
      summary = "Command pattern",
      description = "Demonstrates Command pattern with AWS operations")
  public Map<String, Object> commandExample() {
    LOG.info("Demonstrating Command pattern");

    var s3Strategy = new S3OperationStrategy(s3Client);
    var s3Command = new S3ListBucketsCommand(s3Strategy);

    Map<String, Object> commandResult = commandInvoker.executeCommand(s3Command);

    Map<String, Object> result = new HashMap<>();
    result.put("pattern", "Command");
    result.put("commandResult", commandResult);
    result.put("canUndo", true);

    metricsSubject.notifyObservers("command.execution", 1.0, "pattern=command");

    return result;
  }

  @Get("/observer")
  @Operation(
      summary = "Observer pattern",
      description = "Demonstrates Observer pattern with metrics")
  public Map<String, Object> observerExample() {
    LOG.info("Demonstrating Observer pattern");

    Map<String, Object> result = new HashMap<>();
    result.put("pattern", "Observer");
    result.put("observersCount", 2);
    result.put("message", "Check logs for observer notifications");

    metricsSubject.notifyObservers("observer.demo", 42.0, "pattern=observer", "demo=true");

    return result;
  }
}
