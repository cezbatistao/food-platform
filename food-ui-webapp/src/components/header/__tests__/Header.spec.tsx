import { shallow } from "enzyme";

import Header from "../Header";

describe('Header', () => {

  it("renders the title app name", () => {
      const result = shallow(<Header />);
      expect(result.text()).toEqual('Food Platform');
  });

  it("renders the heading", () => {
    const result = shallow(<Header />);
    expect(result).toMatchSnapshot();
  });

});