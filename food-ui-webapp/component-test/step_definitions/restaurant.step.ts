declare const Given, When, Then;
import { BasePage } from '../page_objects/base.page';
import { RestaurantPage } from '../page_objects/restaurant.page';

const restaurantPage: RestaurantPage = new RestaurantPage();

When('I select the category {string}', (category: string) => {
	restaurantPage.fillCategory(category);
});

Then('I visualize a restaurants list', () => {
  const restaurantNames = ["Pizza Hut", "Bráz Pizzaria", "Domino's Pizza"];
	restaurantPage.verify(restaurantNames);
});
