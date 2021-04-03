Feature: As a user I can get a list of restaurants by category

  Scenario: User call to get a list of restaurant by category
    Given I had a category "pizza"
    When user request a list of restaurant with category
    Then user get success response from restaurant
    And user gets a list of restaurants
