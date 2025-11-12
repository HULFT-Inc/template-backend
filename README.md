# Example Template - Center of Excellence

Java Micronaut REST Service template for new developers.

## Package Structure Requirements
- **MANDATORY**: Use company domain `com.saisontechnologyintl.<project-name>`
- **Example**: `com.saisontechnologyintl.template`
- **Never use**: `com.example.*` or generic packages

## Architecture
- **Framework:** Micronaut 4.2.1
- **Runtime:** Java 17
- **Deployment:** AWS ALB + VPC Lattice
- **Build:** Gradle with Shadow plugin

## CI/CD Pipeline
- **CI Pipeline**: Build, test, quality gates (Checkstyle, SpotBugs, ArchUnit)
- **CD Pipeline**: Docker build, ECR push, ECS deployment
- **Security Pipeline**: OWASP, CodeQL, Trivy vulnerability scanning
- **Branch Protection**: Git Flow enforcement, JIRA commit validation

## Quality Standards
- ✅ 0% Checkstyle violations
- ✅ 0% SpotBugs violations  
- ✅ BDD test coverage required
- ✅ ArchUnit architecture rules enforced

## Quick Start
```bash
# Build project
./control.sh build

# Run locally
./control.sh run

# Run quality checks
./control.sh quality

# Start development environment
./control.sh dev

# Deploy to dev
./control.sh deploy-dev
```

## Development Workflow
1. Start feature: `git flow feature start feature-name`
2. Make changes
3. Build: `./control.sh build`
4. Auto-commit on success: `git add . && git commit -m "JIRA-XXX: feat: description - $(date '+%Y-%m-%d %H:%M:%S')"`
5. Finish feature: `git flow feature finish feature-name`

## Amazon Q Developer Rules
- **JIRA Traceability**: All commits must start with JIRA-XXX:
- **Auto-commit**: Commit after every successful build
- **Quality Gates**: 0% violations required for all quality tools
- **Git Flow**: Mandatory branching model for all changes

## Git Flow Requirements
- **MANDATORY**: Use Git Flow branching model
- **Feature branches**: All changes via `git flow feature start/finish`
- **Semantic commits**: Use conventional commit messages
- **No direct commits**: Never commit directly to main/develop
- See [GITFLOW.md](GITFLOW.md) for complete workflow


## Endpoints
- `GET /template/health` - Health check endpoint
- `GET /template/metrics/test - Test custom metrics
- `GET /template/swagger-ui` - API documentation

## Testing
- **Unit Tests**: JUnit 5 with Mockito and AssertJ
- **Functional Tests**: REST Assured integration tests
- **BDD Tests**: Cucumber with Gherkin scenarios
- **Performance Tests**: JMeter load testing
- **Code Coverage**: JaCoCo with 80% minimum coverage

## Design Patterns (Gang of Four)
- **Factory Pattern**: AWS client creation with configuration
- **Strategy Pattern**: Pluggable AWS operation algorithms
- **Observer Pattern**: Metrics notification system
- **Command Pattern**: AWS operations with undo capability
- **Singleton Pattern**: Application configuration management

## AWS SDK Integration
- **S3**: Object storage operations with X-Ray tracing
- **DynamoDB**: NoSQL database operations with X-Ray tracing
- **SQS**: Message queue operations with X-Ray tracing
- **SNS**: Notification service operations with X-Ray tracing
- **X-Ray**: Distributed tracing for all AWS operations
- **LocalStack**: Local AWS development environment

## Features
- **API Documentation**: OpenAPI/Swagger UI available at `/template/swagger-ui`
- **BDD Testing**: Cucumber tests for behavior verification
- **LocalStack Integration**: DynamoDB, SQS, SNS testing
- **PostgreSQL Testing**: Testcontainers integration

## Context Path
All endpoints are served under `/template` context path.

## Deployment
Uses standard deployment tool: `~/bin/deployer deploy2dev`
# GitHub Actions Test
