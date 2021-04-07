import * as enzyme from 'enzyme';

import { createMount, createShallow } from '@material-ui/core/test-utils';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';

import * as reactRedux from 'react-redux';
import { useDispatch } from 'react-redux';
import { AnyAction } from 'redux';

import RestaurantCategory from "../RestaurantCategory";
import Category from "../../../../domain/Category";

import categoriesResponseJson from "../../../../../dependencies/stubby/mocks/response/categories_response.json";

jest.mock('react-redux', () => ({
  ...jest.requireActual('react-redux'),
  useDispatch: jest.fn()
}));

const useDispatchMock = useDispatch as jest.Mock;

describe('RestaurantCategory', () => {

  let shallow:typeof enzyme.shallow;
  let mount: any;

  const dispatchResultRecorder = {} as any;
  const fakeDispatch = (action: AnyAction) => {
    let payload = action.payload;
    if (payload === undefined) {
      payload = 'void';
    }
    dispatchResultRecorder[action.type] = payload;
  };

  useDispatchMock.mockImplementation(() => fakeDispatch);

  beforeAll(() => {
    shallow = createShallow();
    mount = createMount({strict: true});
  });

  beforeEach(() => {
    useDispatchMock.mockClear();
  });

  afterAll(() => {
    mount.cleanUp();
  });

  it("renders the categories from restaurant", () => {
    const wrapper = shallow(
      <RestaurantCategory 
        loading={ false } 
        categories={ categoriesResponseJson.data as Category[] }
        dispatch={ useDispatchMock } 
      />
    );

    expect(wrapper.find(FormControl).length).toBe(1);
    expect(wrapper.find(MenuItem).length).toBe(5);

    expect(useDispatchMock).toHaveBeenCalledTimes(0);

    expect(wrapper).toMatchSnapshot();
  });

  it("renders empty categories", () => {
    const wrapper = shallow(
      <RestaurantCategory 
        loading={ false } 
        categories={ [] }
        dispatch={ useDispatchMock } 
      />
    );

    expect(wrapper.find(FormControl).length).toBe(1);
    expect(wrapper.find(MenuItem).length).toBe(0);

    expect(useDispatchMock).toHaveBeenCalledTimes(0);

    expect(wrapper).toMatchSnapshot();
  });

  it("renders the components RestaurantCategory and RestaurantList", () => {
    const value = 'pizza';

    const wrapper = shallow(
      <RestaurantCategory 
        loading={ false } 
        categories={ categoriesResponseJson.data as Category[] }
        dispatch={ useDispatchMock } 
      />
    );
    
    wrapper.find(Select).simulate('change', {
      target: { value }
    });

    expect(useDispatchMock).toHaveBeenCalledTimes(1);
  });

});