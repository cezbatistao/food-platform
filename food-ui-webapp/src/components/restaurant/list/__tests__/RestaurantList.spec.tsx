import { shallow } from 'enzyme';

import { useSelector } from 'react-redux';

import RestaurantList from "../RestaurantList";
import RestaurantCard from '../../card/RestaurantCard';

import Restaurant from '../../../../domain/Restaurant';

import restaurantsResponseJson from "../../../../../dependencies/stubby/mocks/response/restaurants_response.json";

const mockUseSelector: jest.Mock = useSelector as any;
const mockUseDispatch = jest.fn();
jest.mock('react-redux', () => ({
  useSelector: jest.fn(),
  useDispatch: () => mockUseDispatch
}));

describe('RestaurantList', () => {

  beforeEach(() => {
    mockUseDispatch.mockClear();
    mockUseSelector.mockClear();
  });

  it("should don't show RestaurantCard", () => {
    mockUseSelector.mockImplementation((selector) => selector({
      restaurant: {
        restaurants: [], 
        error: false, 
        loading: false,
      }
    }));

    const wrapper = shallow(<RestaurantList />)
  
    expect(wrapper.find(RestaurantCard).length).toBe(0);

    expect(wrapper).toMatchSnapshot();
  });

  it("should show RestaurantCard", () => {
    mockUseSelector.mockImplementation((selector) => selector({
      restaurant: {
        restaurants: restaurantsResponseJson.data as Restaurant[], 
        error: false, 
        loading: false,
      }
    }));

    const wrapper = shallow(<RestaurantList />);
  
    expect(wrapper.find(RestaurantCard).length).toBe(3);

    expect(wrapper).toMatchSnapshot();
  });

});