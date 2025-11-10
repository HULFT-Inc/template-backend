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

## Quality Standards
- ✅ 0% Checkstyle violations
- ✅ 0% SpotBugs violations  
- ✅ BDD test coverage required
- ✅ ArchUnit architecture rules enforced

## Quick Start
```bash
# Build project
make build

# Run locally
make run

# Run quality checks
make check

# Deploy to dev
make deploy-dev
```

## Development Workflow
1. Start timetracker: `timetracker start template-backend`
2. Make changes
3. Build: `make build`
4. Auto-commit on success: `git add . && git commit -m "Build successful - $(date '+%Y-%m-%d %H:%M:%S')"`
5. Log time: `timetracker log --q-time X --personal-time Y --category coding`

## Time Tracking Summary
Project: Example Template
Total Time: [X hours]
Q Developer Time: [X hours] 
Personal Time: [X hours]
Estimated Without Q: [X hours]
Efficiency Gain: [X%]
Categories: coding([X]h), debugging([X]h), meetings([X]h), research([X]h)

## Endpoints
- `GET /template/health` - Health check endpoint
- `GET /template/swagger-ui` - API documentation

## Features
- **API Documentation**: OpenAPI/Swagger UI available at `/template/swagger-ui`
- **BDD Testing**: Cucumber tests for behavior verification
- **LocalStack Integration**: DynamoDB, SQS, SNS testing
- **PostgreSQL Testing**: Testcontainers integration

## Context Path
All endpoints are served under `/template` context path.

## Deployment
Uses standard deployment tool: `~/bin/deployer deploy2dev`
