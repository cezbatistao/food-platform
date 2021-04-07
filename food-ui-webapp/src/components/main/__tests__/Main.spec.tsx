import { shallow } from "enzyme";

import Main from "../Main";

import RestaurantCategoryContainer from "../../restaurant/category/RestaurantCategoryContainer";
import RestaurantList from "../../restaurant/list/RestaurantList";

describe('Main', () => {

  it("renders the main", () => {
    const result = shallow(<Main />);
    expect(result).toMatchSnapshot();
  });

  it("renders the components RestaurantCategory and RestaurantList", () => {
    const result = shallow(<Main />);
    expect(result.find(RestaurantCategoryContainer).length).toBe(1);
    expect(result.find(RestaurantList).length).toBe(1);
  });

});