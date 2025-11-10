Feature: Health Check
  As a system administrator
  I want to check the health of the service
  So that I can monitor service availability

  Scenario: Service is healthy
    Given the service is running
    When I request the health endpoint
    Then I should receive a healthy response
    And the response should be "OK"
