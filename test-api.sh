#!/bin/bash

PORT=${1:-8080}
BASE_URL="http://localhost:${PORT}/changetracker"

echo "Testing Change Tracker API on port ${PORT}"
echo "============================================"

# Test 1: Record a PO change for GPS
echo -e "\n1. Recording PO change for GPS..."
curl -s -X POST ${BASE_URL}/changes \
  -H "Content-Type: application/json" \
  -d '{
    "orgShortcode": "GPS",
    "documentType": "PO",
    "documentId": "PO-12345",
    "fieldName": "lineItem",
    "oldValue": "100 units",
    "newValue": "150 units",
    "changedBy": "john.doe",
    "fileName": "purchase_order.pdf",
    "fileType": "PDF",
    "fileLocation": "https://drive.google.com/file/d/abc123",
    "storageSystem": "GOOGLE_DRIVE",
    "systemDetail": "{\"driveId\": \"abc123\"}"
  }' | jq .

# Test 2: Record invoice change for ACME
echo -e "\n2. Recording invoice change for ACME..."
curl -s -X POST ${BASE_URL}/changes \
  -H "Content-Type: application/json" \
  -d '{
    "orgShortcode": "ACME",
    "documentType": "INVOICE",
    "documentId": "INV-9876",
    "fieldName": "dueDate",
    "oldValue": "2025-01-15",
    "newValue": "2025-02-01",
    "changedBy": "jane.smith",
    "fileName": "invoice.xlsx",
    "fileType": "EXCEL",
    "fileLocation": "s3://bucket/invoice.xlsx",
    "storageSystem": "S3",
    "systemDetail": "{\"bucket\": \"invoices\", \"region\": \"us-east-1\"}"
  }' | jq .

# Test 3: Get changes by organization
echo -e "\n3. Getting all changes for GPS..."
curl -s ${BASE_URL}/changes/org/GPS | jq .

# Test 4: Get changes by document type
echo -e "\n4. Getting all INVOICE changes..."
curl -s ${BASE_URL}/changes/type/INVOICE | jq .

# Test 5: Get changes by org and type
echo -e "\n5. Getting GPS PO changes..."
curl -s ${BASE_URL}/changes/org/GPS/type/PO | jq .

# Test 6: Get changes by document ID
echo -e "\n6. Getting changes for document PO-12345..."
curl -s ${BASE_URL}/changes/document/PO-12345 | jq .

echo -e "\n============================================"
echo "API Testing Complete!"
