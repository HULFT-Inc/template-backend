#!/bin/bash

set -e

# Configuration
PROFILE="predev"
REGION="us-east-2"
VPC_ID="vpc-12345678"  # Replace with actual VPC ID
SUBNETS="subnet-12345678,subnet-87654321"  # Replace with actual subnet IDs
IMAGE_URI="123456789012.dkr.ecr.us-east-2.amazonaws.com/template-backend:latest"

echo "🚀 Starting rapid deployment to predev..."

# Build the main application Docker image
echo "📦 Building application image..."
cd ..
docker build -t template-backend:latest .

# Tag and push to ECR (assumes ECR repo exists)
echo "📤 Pushing to ECR..."
aws ecr get-login-password --region $REGION --profile $PROFILE | docker login --username AWS --password-stdin $IMAGE_URI
docker tag template-backend:latest $IMAGE_URI
docker push $IMAGE_URI

# Start the deployer
echo "🔧 Starting deployer service..."
cd deployment
./gradlew run &
DEPLOYER_PID=$!

# Wait for deployer to start
sleep 10

# Trigger deployment
echo "🚀 Triggering deployment..."
curl -X POST "http://localhost:8081/deploy/predev?vpcId=$VPC_ID&imageUri=$IMAGE_URI&subnets=$SUBNETS" \
  -H "Content-Type: application/json" | jq .

# Stop deployer
kill $DEPLOYER_PID

echo "✅ Deployment completed!"
