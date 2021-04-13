import { Then } from "cypress-cucumber-preprocessor/steps";
const basePage = require('../page_objects/base.page');
const proposalsPage = require('../page_objects/restaurant.page');

Then('I select the category {string}', (category) => {
	proposalsPage.fillCategory(category);
});

Then('I visualize a restaurants list', () => {
  const restaurantNames = ["Pizza Hut", "Bráz Pizzaria", "Domino's Pizza"];
	proposalsPage.verify(restaurantNames);
});
