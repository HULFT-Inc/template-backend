# AWS VPC Lattice Rapid Deployment - Ohio (us-east-2)

Java Micronaut-based deployment tool for AWS VPC Lattice with ECS Fargate in Ohio region.

## Features

- **Complete Infrastructure Setup**: Automatically creates VPC, subnets, security groups, ECR, IAM roles
- **VPC Lattice**: Service mesh deployment with service networks
- **ECS Fargate**: Serverless container deployment
- **AWS SDK v2**: Modern AWS integration
- **Ohio Region**: Deployed in us-east-2 for optimal performance
- **Profile Support**: Uses AWS CLI profiles (predev)

## Quick Start

### Prerequisites
```bash
# Configure AWS CLI with predev profile
aws configure --profile predev

# Set AWS account ID (for IAM role ARN)
export AWS_ACCOUNT_ID=$(aws sts get-caller-identity --profile predev --query Account --output text)
```

### Automatic Setup (Recommended)
```bash
# From main project directory - sets up everything automatically
./control.sh deploy-predev

# Or directly from deployment directory
cd deployment
./rapid-deploy --setup-all
```

### Manual Setup (Advanced)
```bash
# Set your own infrastructure parameters
export VPC_ID="vpc-12345678"
export SUBNETS="subnet-12345678,subnet-87654321"
export IMAGE_URI="123456789012.dkr.ecr.us-east-2.amazonaws.com/template-backend:latest"

# Deploy with manual parameters
./control.sh deploy-predev --manual
```

### Check Status
```bash
./control.sh deploy-status
```

## Infrastructure Created

### VPC Components
- **VPC**: `10.0.0.0/16` CIDR block
- **Public Subnets**: `10.0.1.0/24` (us-east-2a), `10.0.2.0/24` (us-east-2b)
- **Internet Gateway**: For public internet access
- **Route Table**: Routes traffic to internet gateway
- **Security Group**: Allows HTTP traffic on ports 80 and 8080

### AWS Services
- **ECR Repository**: `template-backend` for container images
- **IAM Role**: `ecsTaskExecutionRole` with required policies
- **CloudWatch Log Group**: `/ecs/template-backend` for container logs

### VPC Lattice Components
- **Service Network**: `template-backend-predev`
- **Service**: `template-backend-service`
- **Target Group**: `template-backend-targets`

### ECS Components
- **Cluster**: `template-backend-predev`
- **Task Definition**: `template-backend` (Fargate, 256 CPU, 512 MB)
- **Service**: `template-backend-service`

## API Endpoints

- `POST /deploy/setup` - Setup complete infrastructure
- `POST /deploy/predev` - Deploy to predev environment
- `POST /deploy/status` - Get deployment status

## Configuration

### Environment Variables
- `AWS_PROFILE` - AWS profile (default: predev)
- `AWS_REGION` - AWS region (default: us-east-2)
- `AWS_ACCOUNT_ID` - AWS account ID (required for IAM roles)

### Optional Manual Parameters
- `VPC_ID` - Target VPC ID
- `SUBNETS` - Comma-separated subnet IDs
- `IMAGE_URI` - ECR image URI

## Development

### Build
```bash
./gradlew build
```

### Run Locally
```bash
./gradlew run
```

### Test Infrastructure Setup
```bash
curl -X POST "http://localhost:8081/deploy/setup"
```

### Test Deployment
```bash
curl -X POST "http://localhost:8081/deploy/predev"
```

## Troubleshooting

### Common Issues
1. **IAM Permissions**: Ensure predev profile has permissions for VPC, ECS, ECR, IAM, CloudWatch
2. **Account ID**: Set `AWS_ACCOUNT_ID` environment variable
3. **Region**: Deployment is configured for us-east-2 (Ohio)

### Required IAM Permissions
- `ec2:*` (VPC, subnets, security groups)
- `ecs:*` (clusters, services, task definitions)
- `ecr:*` (repositories)
- `iam:*` (roles and policies)
- `logs:*` (CloudWatch log groups)
- `vpc-lattice:*` (service networks, services, target groups)
