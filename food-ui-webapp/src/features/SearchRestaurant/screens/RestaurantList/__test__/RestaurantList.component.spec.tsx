import { mocked } from 'ts-jest/utils';
import { render, screen, waitFor } from '@testing-library/react';

import { fetchRestaurantsByCategory } from '../../../../../services/Restaurant.services';
import RestaurantList from '../RestaurantList.component';
import Error from '../../../../../services/Error.model';
import { showWarning } from '../../../../../services/Notification.service';
import restaurantsResponseJson from "../../../../../../dependencies/stubby/mocks/response/restaurants_response.json";
import Restaurant from '../../../../../services/Restaurant.model';
import CategoryContext from '../../../contexts/RestaurantCategory/RestaurantCategory.context';


jest.mock('../../../../../services/Restaurant.services');
jest.mock('../../../../../services/Notification.service');


let mockedFetchRestaurantsByCategory: any
let mockedShowWarning: any

beforeEach(() => {
  jest.clearAllMocks();
  mockedFetchRestaurantsByCategory = mocked(fetchRestaurantsByCategory);
  mockedShowWarning = mocked(showWarning);
});

it('should list restaurants empty list on component when no select category', async () => {

  mockedFetchRestaurantsByCategory.mockImplementationOnce((category: string) => Promise.resolve(
    []
  ));

  render(
    <RestaurantList />
  );

  const restaurantList = await waitFor(() => screen.getByTestId('restaurant-list')) as HTMLDivElement;

  expect(restaurantList.childElementCount).toEqual(0);
  expect(mockedFetchRestaurantsByCategory).not.toHaveBeenCalled()
});

it('should list restaurants empty list on component when select category without restaurants', async () => {

  mockedFetchRestaurantsByCategory.mockImplementationOnce((category: string) => Promise.resolve(
    []
  ));

  const categoryState = {
    category: 'hamburger',
    setCategory: jest.fn(),
  };

  render(
    <CategoryContext.Provider value={ categoryState }>
      <RestaurantList />
    </CategoryContext.Provider>
  );

  const restaurantList = await waitFor(() => screen.getByTestId('restaurant-list')) as HTMLDivElement;

  expect(restaurantList.childElementCount).toEqual(0);
  expect(mockedFetchRestaurantsByCategory).toHaveBeenCalled();
});

it('should list restaurants list on component', async () => {

  mockedFetchRestaurantsByCategory.mockImplementationOnce((category: string) => Promise.resolve(
    restaurantsResponseJson.data as Restaurant[]
  ));

  const categoryState = {
    category: 'pizza',
    setCategory: jest.fn(),
  };

  render(
    <CategoryContext.Provider value={ categoryState }>
      <RestaurantList />
    </CategoryContext.Provider>
  );

  const restaurantList = await waitFor(() => screen.getByTestId('restaurant-list')) as HTMLDivElement;

  expect(restaurantList.childElementCount).toEqual(3);
  expect(mockedFetchRestaurantsByCategory).toHaveBeenCalled();
});

it('should show warning message when fetch restaurants get error', async () => {

  mockedFetchRestaurantsByCategory.mockImplementationOnce((category: string) => Promise.reject(
    new Error("0003", "Error fetch restaurants")
  ));

  mockedShowWarning.mockImplementationOnce((message: string) => {
    expect(message).toEqual('Error fetch restaurants');
  });

  render(
    <RestaurantList />
  );

  const restaurantList = await waitFor(() => screen.getByTestId('restaurant-list')) as HTMLDivElement;

  expect(restaurantList.childElementCount).toEqual(0);
  expect(mockedFetchRestaurantsByCategory).not.toHaveBeenCalled();
});