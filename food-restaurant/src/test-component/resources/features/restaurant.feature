Feature: As a user I can get a list of restaurants by category

  Scenario: User call to get a list of restaurant by category
    Given I had a "pizza" category
    When user request a list of restaurant with category
    Then user get success response
    And user gets a list of restaurants with content "200-restaurants.json"

  Scenario: User call to get a list of restaurant by category without exists
    Given I had a "hamburguer" category
    When user request a list of restaurant with category
    Then user get success response
    And user gets a list of restaurants with content "200-empty-restaurants.json"

  Scenario: User calls for details of a non-existent restaurant
    Given I had unique id "f5b26236-c6e3-4101-95c8-adad7556dbc1" from restaurant
    When user request for detail restaurant with unique id
    Then user gets a not found response
    And user gets a error detail from restaurant "404-detail-restaurant.json"

  Scenario: User calls for details of a restaurant
    Given I had unique id "23e0211c-19ea-47ee-b98e-77e023e1a95f" from restaurant
    When user request for detail restaurant with unique id
    Then user get success response
    And user gets a detail from restaurant "200-detail-restaurant.json"
