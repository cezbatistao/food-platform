Feature: As a user I can get all categories from restaurants

  Scenario: User call to get all categories
    When user request all categories
    Then user get success response from category
    And user gets categories from restaurants
