# Multi-stage build for Java Micronaut application
FROM amazoncorretto:17-alpine AS builder

WORKDIR /app
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./
COPY src/ src/

# Build application
RUN ./gradlew shadowJar --no-daemon

# Runtime stage
FROM amazoncorretto:17-alpine AS runtime

# Install curl for health checks
RUN apk add --no-cache curl

# Create non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Copy built jar
COPY --from=builder /app/build/libs/*-all.jar app.jar

# Create logs directory
RUN mkdir -p logs && chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/template/health || exit 1

# Run application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
