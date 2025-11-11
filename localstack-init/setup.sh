#!/bin/bash

echo "Setting up LocalStack resources for template service..."

# Create S3 bucket
awslocal s3 mb s3://template-bucket

# Create DynamoDB table
awslocal dynamodb create-table \
    --table-name template-test \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST

# Create SQS queue
awslocal sqs create-queue --queue-name template-queue

# Create SNS topic
awslocal sns create-topic --name template-topic

echo "LocalStack setup completed!"
