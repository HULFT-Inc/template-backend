# Example Template - Center of Excellence

Java Micronaut REST Service template for new developers.

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

## Context Path
All endpoints are served under `/template` context path.

## Deployment
Uses standard deployment tool: `~/bin/deployer deploy2dev`
