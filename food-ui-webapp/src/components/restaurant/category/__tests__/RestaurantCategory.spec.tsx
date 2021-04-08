import * as enzyme from 'enzyme';

import { createMount, createShallow } from '@material-ui/core/test-utils';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';

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

  let shallow:typeof enzyme.shallow;
  let mount: any;

  beforeAll(() => {
    shallow = createShallow();
    mount = createMount({strict: true});
  });

  beforeEach(() => {
    mockUseDispatch.mockClear();
    mockGetRestaurantsByCategory.mockClear();
  });

  afterAll(() => {
    mount.cleanUp();
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

    expect(wrapper.find(FormControl).length).toBe(1);
    expect(wrapper.find(MenuItem).length).toBe(5);
    
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

    expect(wrapper.find(FormControl).length).toBe(1);
    expect(wrapper.find(MenuItem).length).toBe(0);

    expect(mockUseDispatch).toHaveBeenCalledTimes(0);
    expect(mockGetRestaurantsByCategory).toHaveBeenCalledTimes(0);

    expect(wrapper).toMatchSnapshot();
  });

  it("renders the components RestaurantCategory and RestaurantList", async () => {
    const valueToSelect = 'pizza';

    mockUseDispatch.mockReturnValue(jest.fn());
    mockGetRestaurantsByCategory.mockReturnValue(jest.fn());

    const wrapper = shallow(
      <RestaurantCategory 
        loading={ false } 
        categories={ categoriesResponseJson.data as Category[] }
      />
    );
    
    wrapper.find(Select).simulate('change', {
      target: { value: valueToSelect }
    });

    expect(mockUseDispatch).toHaveBeenCalledTimes(1);
    expect(mockGetRestaurantsByCategory).toHaveBeenCalledTimes(1);

    expect(mockGetRestaurantsByCategory.mock.calls[0][0]).toBe(valueToSelect)
  });
});