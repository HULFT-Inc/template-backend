Feature: Change Management Record System
  Track changes to business documents by organization

  Background:
    Given the application is running
    And organizations "GPS" and "ACME" exist

  Scenario: Record a change to a PO
    When I record a change for organization "GPS"
      | documentType | PO           |
      | documentId   | PO-12345     |
      | fieldName    | lineItem     |
      | oldValue     | 100 units    |
      | newValue     | 150 units    |
      | changedBy    | john.doe     |
    Then the change is recorded successfully

  Scenario: Query changes by organization
    Given changes exist for organization "GPS"
    When I query changes for organization "GPS"
    Then I receive all changes for "GPS"

  Scenario: Query changes by document type
    Given changes exist for document type "INVOICE"
    When I query changes for document type "INVOICE"
    Then I receive all invoice changes

  Scenario: Query changes by organization and document type
    Given changes exist for organization "ACME" and document type "PO"
    When I query changes for organization "ACME" and document type "PO"
    Then I receive filtered changes

  Scenario: Track invoice due date change
    When I record a change for organization "ACME"
      | documentType | INVOICE      |
      | documentId   | INV-9876     |
      | fieldName    | dueDate      |
      | oldValue     | 2025-01-15   |
      | newValue     | 2025-02-01   |
      | changedBy    | jane.smith   |
    Then the change is recorded successfully
    And I can query changes for document "INV-9876"
