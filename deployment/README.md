# AWS VPC Lattice Rapid Deployment

Java Micronaut-based deployment tool for AWS VPC Lattice with ECS Fargate.

## Features

- **VPC Lattice**: Service mesh deployment with service networks
- **ECS Fargate**: Serverless container deployment
- **AWS SDK v2**: Modern AWS integration
- **Rapid Deployment**: Single command deployment to predev
- **Profile Support**: Uses AWS CLI profiles (predev)

## Quick Start

### Prerequisites
```bash
# Configure AWS CLI with predev profile
aws configure --profile predev

# Set deployment parameters
export VPC_ID="vpc-12345678"
export SUBNETS="subnet-12345678,subnet-87654321"
export IMAGE_URI="123456789012.dkr.ecr.us-east-1.amazonaws.com/template-backend:latest"
```

### Deploy to Predev
```bash
# From main project directory
./control.sh deploy-predev

# Or directly from deployment directory
cd deployment
./rapid-deploy --vpc-id $VPC_ID --subnets $SUBNETS --image-uri $IMAGE_URI
```

### Check Status
```bash
./control.sh deploy-status
```

## Architecture

### VPC Lattice Components
- **Service Network**: `template-backend-predev`
- **Service**: `template-backend-service`
- **Target Group**: `template-backend-targets`

### ECS Components
- **Cluster**: `template-backend-predev`
- **Task Definition**: `template-backend`
- **Service**: `template-backend-service`

## API Endpoints

- `POST /deploy/predev` - Deploy to predev environment
- `POST /deploy/status` - Get deployment status

## Configuration

### Environment Variables
- `AWS_PROFILE` - AWS profile (default: predev)
- `AWS_REGION` - AWS region (default: us-east-1)
- `VPC_ID` - Target VPC ID
- `SUBNETS` - Comma-separated subnet IDs
- `IMAGE_URI` - ECR image URI

### AWS Resources Required
- VPC with public subnets
- ECR repository
- ECS task execution role
- Appropriate IAM permissions

## Development

### Build
```bash
./gradlew build
```

### Run Locally
```bash
./gradlew run
```

### Test Deployment
```bash
curl -X POST "http://localhost:8081/deploy/predev?vpcId=vpc-123&imageUri=image:latest&subnets=subnet-123"
```
