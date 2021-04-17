import { shallow } from 'enzyme';

import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';

import RestaurantCategory from "../RestaurantCategory";
import Category from "../../../../domain/Category";

import categoriesResponseJson from "../../../../../dependencies/stubby/mocks/response/categories_response.json";

const mockUseDispatch = jest.fn();
jest.mock('react-redux', () => ({
  useSelector: jest.fn(),
  useDispatch: () => mockUseDispatch
}));

const mockGetRestaurantsByCategory = jest.fn();
jest.mock('../../../../gateway/actions/restaurant.actions', () => ({
  getRestaurantsByCategory: (category: string) => mockGetRestaurantsByCategory(category)
}));

describe('RestaurantCategory', () => {

  beforeEach(() => {
    mockUseDispatch.mockClear();
    mockGetRestaurantsByCategory.mockClear();
  });

  it("renders the categories from restaurant", () => {
    mockUseDispatch.mockReturnValue(jest.fn());
    mockGetRestaurantsByCategory.mockReturnValue(jest.fn());

    const wrapper = shallow(
      <RestaurantCategory 
        loading={ false } 
        categories={ categoriesResponseJson.data as Category[] }
      />
    );

    expect(wrapper.find('select').length).toBe(1);
    expect(wrapper.find('select option').length).toBe(6);
    
    expect(mockUseDispatch).toHaveBeenCalledTimes(0);
    expect(mockGetRestaurantsByCategory).toHaveBeenCalledTimes(0);

    expect(wrapper).toMatchSnapshot();
  });

  it("renders empty categories", () => {
    mockUseDispatch.mockReturnValue(jest.fn());
    mockGetRestaurantsByCategory.mockReturnValue(jest.fn());

    const wrapper = shallow(
      <RestaurantCategory 
        loading={ false } 
        categories={ [] }
      />
    );

    expect(wrapper.find('select').length).toBe(1);
    expect(wrapper.find('select option').length).toBe(1);

    expect(mockUseDispatch).toHaveBeenCalledTimes(0);
    expect(mockGetRestaurantsByCategory).toHaveBeenCalledTimes(0);

    expect(wrapper).toMatchSnapshot();
  });

  it("should dispatch a getRestaurantsByCategory when select a category of restaurant", () => {
    const valueToSelect = 'pizza';

    mockUseDispatch.mockReturnValue(jest.fn());
    mockGetRestaurantsByCategory.mockReturnValue(jest.fn());

    const wrapper = shallow(
      <RestaurantCategory 
        loading={ false } 
        categories={ categoriesResponseJson.data as Category[] }
      />
    );
    
    wrapper.find('select').simulate('change', {
      target: { value: valueToSelect }
    });

    expect(mockUseDispatch).toHaveBeenCalledTimes(1);
    expect(mockGetRestaurantsByCategory).toHaveBeenCalledTimes(1);

    expect(mockGetRestaurantsByCategory.mock.calls[0][0]).toBe(valueToSelect)
  });
});