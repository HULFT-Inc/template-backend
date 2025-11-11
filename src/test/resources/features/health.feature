Feature: Health Check
  As a system administrator
  I want to check the health of the service
  So that I can monitor service availability

  Scenario: Health check returns OK
    Given the service is running
    When I request the health endpoint
    Then I should receive an OK response
    And the response status should be 200

  Scenario: Health check increments metrics
    Given the service is running
    When I request the health endpoint multiple times
    Then the health check counter should be incremented
