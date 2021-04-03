Feature: As a user I can get a list of restaurants by category

  Scenario: User call to get a list of restaurant by category
    Given I had a category "pizza"
    When user request a list of restaurant with category
    Then user get success response
    And user gets a list of restaurants with content "200-restaurants.json"

  Scenario: User call to get a list of restaurant by category without exists
    Given I had a category "hamburguer"
    When user request a list of restaurant with category
    Then user get success response
    And user gets a list of restaurants with content "200-empty-restaurants.json"