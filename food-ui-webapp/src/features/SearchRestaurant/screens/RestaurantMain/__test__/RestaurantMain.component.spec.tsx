import { render, screen, waitFor, within } from '@testing-library/react';
import {Router} from 'react-router-dom';
import {createMemoryHistory} from 'history';

import RestaurantMain from '../RestaurantMain.component';
import restaurantsResponseJson from "../../../../../../dependencies/stubby/mocks/response/restaurants_response.json";
import Restaurant from '../../../../../services/Restaurant.model';


beforeEach(() => {
  jest.clearAllMocks();
});

it('should show restaurant main page with restaurant filter and restaurant list components', async () => {

  const history = createMemoryHistory();

  render(
    <Router history={history}>
      <RestaurantMain />
    </Router>
  );

  const restaurantMain = await waitFor(() => screen.getByTestId('restaurant-main')) as HTMLDivElement;
  
  const restaurantCategory = within(restaurantMain).getAllByTestId('select-category') as HTMLSelectElement[];
  expect(restaurantCategory.length).toBe(1);

  const restaurantList = within(restaurantMain).getAllByTestId('restaurant-list') as HTMLDivElement[];
  expect(restaurantList.length).toBe(1);
});