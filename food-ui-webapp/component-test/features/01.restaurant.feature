@component @mocked
Feature: Tela listagem de restaurantes

  Background:
    When I access application
    Then I redirect to "/home"

  Scenario: Comportamento do botão "INICIAR ANÁLISE"
    When I select the category "Pizzaria"
    Then I visualize a restaurants list
