#!/bin/bash

# Template Backend Control Script
# Center of Excellence - Saison Technology International

set -e

PROJECT_NAME="template-backend"
DOCKER_IMAGE="template-backend"
DOCKER_TAG="latest"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
    exit 1
}

info() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')] INFO: $1${NC}"
}

show_help() {
    echo "Template Backend Control Script"
    echo ""
    echo "Usage: ./control.sh [COMMAND]"
    echo ""
    echo "Commands:"
    echo "  build         Build the application"
    echo "  test          Run all tests"
    echo "  quality       Run quality checks"
    echo "  run           Run the application locally"
    echo "  docker-build  Build Docker image"
    echo "  docker-run    Run Docker container"
    echo "  clean         Clean build artifacts"
    echo "  format        Apply code formatting"
    echo "  dev           Start development environment"
    echo "  deploy-dev    Deploy to development"
    echo "  status        Show application status"
    echo "  logs          Show application logs"
    echo "  help          Show this help message"
}

build() {
    log "Building $PROJECT_NAME..."
    ./gradlew build
    log "Build completed successfully"
}

test() {
    log "Running tests for $PROJECT_NAME..."
    ./gradlew test
    log "Tests completed successfully"
}

quality() {
    log "Running quality checks..."
    ./gradlew checkstyleMain spotbugsMain
    log "Quality checks passed"
}

run_app() {
    log "Starting $PROJECT_NAME..."
    ./gradlew run
}

docker_build() {
    log "Building Docker image: $DOCKER_IMAGE:$DOCKER_TAG"
    if [ ! -f Dockerfile ]; then
        warn "Dockerfile not found, creating basic Dockerfile..."
        create_dockerfile
    fi
    docker build -t $DOCKER_IMAGE:$DOCKER_TAG .
    log "Docker image built successfully"
}

docker_run() {
    log "Running Docker container..."
    docker run -p 8080:8080 --name $PROJECT_NAME $DOCKER_IMAGE:$DOCKER_TAG
}

clean() {
    log "Cleaning build artifacts..."
    ./gradlew clean
    rm -rf logs/*.log
    log "Clean completed"
}

format() {
    log "Applying code formatting..."
    ./gradlew spotlessApply
    log "Code formatting applied"
}

dev() {
    log "Starting development environment..."
    format
    build
    test
    quality
    log "Development environment ready"
    run_app
}

deploy_dev() {
    log "Deploying to development environment..."
    if command -v ~/bin/deployer &> /dev/null; then
        ~/bin/deployer deploy2dev
    else
        warn "Deployer not found, using Docker build..."
        docker_build
        info "Docker image ready for deployment"
    fi
}

status() {
    log "Checking application status..."
    if pgrep -f "template" > /dev/null; then
        log "Application is running"
    else
        warn "Application is not running"
    fi
}

show_logs() {
    log "Showing application logs..."
    if [ -f logs/template.log ]; then
        tail -f logs/template.log
    else
        warn "Log file not found"
    fi
}

create_dockerfile() {
    cat > Dockerfile << 'DOCKERFILE_EOF'
FROM openjdk:17-jre-slim

WORKDIR /app

COPY build/libs/*-all.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
DOCKERFILE_EOF
    log "Basic Dockerfile created"
}

# Main script logic
case "${1:-help}" in
    build)
        build
        ;;
    test)
        test
        ;;
    quality)
        quality
        ;;
    run)
        run_app
        ;;
    docker-build)
        docker_build
        ;;
    docker-run)
        docker_run
        ;;
    clean)
        clean
        ;;
    format)
        format
        ;;
    dev)
        dev
        ;;
    deploy-dev)
        deploy_dev
        ;;
    status)
        status
        ;;
    logs)
        show_logs
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        error "Unknown command: $1. Use './control.sh help' for usage information."
        ;;
esac
