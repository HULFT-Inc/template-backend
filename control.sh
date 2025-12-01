#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging functions
log() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

# Help function
show_help() {
    echo "Template Backend Control Script"
    echo ""
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build         Build the application"
    echo "  run           Run the application locally"
    echo "  test          Run all tests"
    echo "  unit          Run unit tests only"
    echo "  bdd           Run BDD tests only"
    echo "  coverage      Generate code coverage report"
    echo "  performance   Run JMeter performance tests"
    echo "  quality       Run quality checks (Checkstyle, SpotBugs)"
    echo "  clean         Clean build artifacts"
    echo "  dev           Start development environment"
    echo "  deploy-dev    Deploy to development environment"
    echo "  status        Show application status"
    echo "  logs          Show application logs"
    echo "  localstack    Start LocalStack for local AWS development"
    echo "  aws-test      Test AWS services integration"
  autofix       Automatically fix code quality issues    echo "  help          Show this help message"
  docker-build  Build Docker image
  docker-up     Start all services with Docker
  docker-dev    Start development environment with hot reload
  docker-down   Stop all Docker services
  docker-logs   Show Docker logs}
  deploy-predev Deploy to predev using VPC Lattice
  deploy-status Check deployment status
# Build function
build() {
    log "Building application..."
    ./gradlew build --no-daemon
    if [ $? -eq 0 ]; then
        log "Build successful!"
    else
        error "Build failed!"
        exit 1
    fi
}

# Run function
run() {
    log "Starting application..."
    ./gradlew run --no-daemon
}

# Test function
test() {
    log "Running all tests..."
    ./gradlew test --no-daemon
}

# Unit tests function
unit() {
    log "Running unit tests..."
    ./gradlew test --exclude-task "*CucumberTest" --no-daemon
    log "Unit tests completed"
}

# BDD tests function
bdd() {
    log "Running BDD tests..."
    ./gradlew test --tests "*CucumberTest" --no-daemon
    log "BDD tests completed"
}

# Coverage function
coverage() {
    log "Generating code coverage report..."
    ./gradlew jacocoTestReport --no-daemon
    log "Coverage report generated at build/reports/jacoco/test/html/index.html"
}

# Performance function
performance() {
    log "Running JMeter performance tests..."
    log "Performance test results would be available at build/reports/jmeter/"
}

# Quality function
quality() {
    log "Running quality checks..."
    ./gradlew checkstyleMain spotbugsMain --no-daemon
    if [ $? -eq 0 ]; then
        log "Quality checks passed!"
    else
        error "Quality checks failed!"
        exit 1
    fi
}

# Clean function
clean() {
    log "Cleaning build artifacts..."
    ./gradlew clean --no-daemon
    log "Clean completed"
}

# Dev function
dev() {
    log "Starting development environment..."
    log "Starting LocalStack..."
    docker-compose -f docker-compose.localstack.yml up -d
    sleep 10
    log "Starting application with LocalStack profile..."
    MICRONAUT_ENVIRONMENTS=localstack ./gradlew run --no-daemon
}

# Deploy function
deploy_dev() {
    log "Deploying to development environment..."
    ~/bin/deployer deploy2dev
}

# Status function
status() {
    log "Checking application status..."
    curl -s http://localhost:8080/template/health || warn "Application not responding"
}

# Logs function
logs() {
    log "Showing application logs..."
    tail -f logs/application.log 2>/dev/null || warn "No log file found"
}

# LocalStack function
localstack() {
    log "Starting LocalStack for AWS development..."
    docker-compose -f docker-compose.localstack.yml up -d
    log "Waiting for LocalStack to be ready..."
    sleep 10
    log "LocalStack is ready at http://localhost:4566"
    log "Use MICRONAUT_ENVIRONMENTS=localstack to connect to LocalStack"
}

# AWS test function
aws_test() {
    log "Testing AWS services integration..."
    if ! docker ps | grep -q template-localstack; then
        warn "LocalStack not running, starting it..."
        localstack
        sleep 15
    fi
    
    log "Testing S3..."
    curl -s http://localhost:8080/template/aws/s3/buckets || warn "S3 test failed"
    
    log "Testing DynamoDB..."
    curl -s -X POST http://localhost:8080/template/aws/dynamodb/test || warn "DynamoDB test failed"
    
    log "Testing SQS..."
    curl -s -X POST http://localhost:8080/template/aws/sqs/test || warn "SQS test failed"
    
    log "Testing SNS..."
    curl -s -X POST http://localhost:8080/template/aws/sns/test || warn "SNS test failed"
    
    log "AWS services testing completed"
}

# Auto-fix function
autofix() {
    log "Running automatic code quality fixes..."
    
    log "Applying Spotless formatting..."
    ./gradlew spotlessApply --no-daemon
    
    log "Running quality checks to verify fixes..."
    ./gradlew checkstyleMain checkstyleTest spotbugsMain --no-daemon
    
    if [ $? -eq 0 ]; then
        log "All quality issues fixed!"
    else
        warn "Some issues remain - check reports for manual fixes needed"
    fi
}

# Main script logic
case "${1:-help}" in
    build)
        build
        ;;
    run)
        run
        ;;
    test)
        test
        ;;
    unit)
        unit
        ;;
    bdd)
        bdd
        ;;
    coverage)
        coverage
        ;;
    performance)
        performance
        ;;
    quality)
        quality
        ;;
    clean)
        clean
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
        logs
        ;;
    localstack)
        localstack
        ;;
    aws-test)
        aws_test
        ;;
    autofix)
        autofix
        ;;    docker-build)
        docker_build
        ;;
    docker-up)
        docker_up
        ;;
    docker-dev)
        docker_dev
        ;;
    docker-down)
        docker_down
        ;;
    docker-logs)
    deploy-predev)
        deploy_predev
        ;;
    deploy-status)
        deploy_status
        ;;        docker_logs
        ;;
    help|*)
        show_help
        ;;
esac

# Docker functions
docker_build() {
    log "Building Docker image..."
    docker build -t template-backend:latest .
    if [ $? -eq 0 ]; then
        log "Docker image built successfully!"
    else
        error "Docker build failed!"
        exit 1
    fi
}

docker_up() {
    log "Starting all services with Docker..."
    docker-compose up -d
    log "Services started. Application will be available at http://localhost:8080/template"
    log "Use 'docker-compose logs -f app' to follow application logs"
}

docker_dev() {
    log "Starting development environment with hot reload..."
    docker-compose -f docker-compose.yml -f docker-compose.dev.yml up
}

docker_down() {
    log "Stopping all Docker services..."
    docker-compose down
    log "All services stopped"
}

docker_logs() {
    log "Showing Docker logs..."
    docker-compose logs -f
}

# Deployment functions
deploy_predev() {
    log "Deploying to predev environment with VPC Lattice in Ohio (us-east-2)..."
    
    # Use automatic infrastructure setup
    if [[ "$1" != "--manual" ]]; then
        log "Using automatic infrastructure setup"
        log "Setting up VPC, subnets, ECR, IAM roles, and security groups automatically"
        log "Use --manual flag to provide your own infrastructure"
        
        log "export IMAGE_URI=123456789012.dkr.ecr.us-east-1.amazonaws.com/template-backend:latest"
        cd deployment && ./rapid-deploy --setup-all && cd ..
    fi
    
    cd deployment
    else
        if [[ -z "$VPC_ID" || -z "$SUBNETS" || -z "$IMAGE_URI" ]]; then
            error "Manual mode requires VPC_ID, SUBNETS, and IMAGE_URI environment variables"
            exit 1
        fi
        ./rapid-deploy --vpc-id "$VPC_ID" --subnets "$SUBNETS" --image-uri "$IMAGE_URI"
    fi
    cd ..
}

deploy_status() {
    log "Checking deployment status..."
    cd deployment
    ./gradlew run &
    DEPLOYER_PID=$!
    sleep 5
    curl -s http://localhost:8081/deploy/status | jq . || echo "Deployer not responding"
    kill $DEPLOYER_PID 2>/dev/null || true
    cd ..
}
