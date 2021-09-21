import { render, screen, waitFor } from '@testing-library/react';

import RestaurantCard from '../RestaurantCard.component';
import restaurantsResponseJson from "../../../../../../dependencies/stubby/mocks/response/restaurants_response.json";
import Restaurant from '../../../../../services/Restaurant.model';


beforeEach(() => {
  jest.clearAllMocks();
});

it('should list restaurants empty list on component', async () => {

  const restaurant: Restaurant = restaurantsResponseJson.data[0] as Restaurant;

  render(
    <RestaurantCard 
      uuid={ restaurant.uuid } 
      name={ restaurant.name } 
      logo={ restaurant.logo }
      description={ restaurant.description }
      address={ restaurant.address }
    />
  );

  const restaurantCard = await waitFor(() => screen.getByTestId(`restaurant_card_${restaurant.uuid}`)) as HTMLDivElement;

  expect(restaurantCard.childElementCount).toEqual(1);

  const restaurantCardContainer = restaurantCard.getElementsByTagName('div')[0];

  expect(restaurantCardContainer.childElementCount).toEqual(3);

  const restaurantCardContainerNameRestaurant = restaurantCardContainer.getElementsByTagName('div')[0];
  const restaurantCardContainerImgRestaurant = restaurantCardContainer.getElementsByTagName('img')[0];

  expect(restaurantCardContainerNameRestaurant.textContent).toEqual('Pizza Hut');
  expect(restaurantCardContainerImgRestaurant.src).toEqual('https://pbs.twimg.com/profile_images/1333417326704791553/Mm0bj3oN.jpg');
  expect(restaurantCardContainerImgRestaurant.alt).toEqual('Pizza Hut');

  const restaurantCardContainerInfoRestaurant = restaurantCardContainer.getElementsByTagName('div')[1];

  expect(restaurantCardContainerInfoRestaurant.childElementCount).toEqual(3);

  const restaurantCardContainerInfoRestaurantAddress = restaurantCardContainerInfoRestaurant.getElementsByTagName('h6')[0];
  const restaurantCardContainerInfoRestaurantRating = restaurantCardContainerInfoRestaurant.getElementsByTagName('div')[0];

  expect(restaurantCardContainerInfoRestaurantAddress.textContent).toEqual('Av. Nome da avenida, 123');
  expect(restaurantCardContainerInfoRestaurantRating.textContent).toEqual('TODO: Add stars from rating here!');

});