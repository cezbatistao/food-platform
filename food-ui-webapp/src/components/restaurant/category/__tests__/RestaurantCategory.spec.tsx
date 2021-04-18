import { shallow, mount } from 'enzyme';

import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';

import RestaurantCategory from "../RestaurantCategory";
import Category from "../../../../domain/Category";

import categoriesResponseJson from "../../../../../dependencies/stubby/mocks/response/categories_response.json";
import React from 'react';

const mockUseDispatch = jest.fn();
jest.mock('react-redux', () => ({
  useSelector: jest.fn(),
  useDispatch: () => mockUseDispatch, 
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
        categoryFromParameter="" 
      />
    );

    expect(wrapper.find('select').length).toBe(1);
    expect(wrapper.find('select option').length).toBe(6);

    expect(wrapper.find('select option').at(0).text()).toEqual("Selecione uma categoria");
    expect(wrapper.find('select option').at(1).text()).toEqual("Pizzaria");
    expect(wrapper.find('select option').at(2).text()).toEqual("Hambúrguer");
    expect(wrapper.find('select option').at(3).text()).toEqual("Japonesa");
    expect(wrapper.find('select option').at(4).text()).toEqual("Vegetariana");
    expect(wrapper.find('select option').at(5).text()).toEqual("Brasileira");
    
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
        categoryFromParameter="" 
      />
    );

    expect(wrapper.find('select').length).toBe(1);
    expect(wrapper.find('select option').length).toBe(1);

    expect(wrapper.find('select option').at(0).text()).toEqual("Selecione uma categoria");

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
        categoryFromParameter="" 
      />
    );
    
    wrapper.find('select').simulate('change', {
      target: { value: valueToSelect }
    });

    expect(wrapper.find('select').props().value).toBe(valueToSelect)

    expect(mockUseDispatch).toHaveBeenCalledTimes(1);
    expect(mockGetRestaurantsByCategory).toHaveBeenCalledTimes(1);

    expect(mockGetRestaurantsByCategory.mock.calls[0][0]).toBe(valueToSelect)
  });

  it("should select a value from categoryFromParameter and dispatch get restaurants by a category with valid value", () => {
    const valueToSelect = 'pizza';

    mockUseDispatch.mockReturnValue(jest.fn());
    mockGetRestaurantsByCategory.mockReturnValue(jest.fn());

    const wrapper = mount(
      <RestaurantCategory 
        loading={ false } 
        categories={ categoriesResponseJson.data as Category[] }
        categoryFromParameter={ valueToSelect }
      />
    );

    expect(wrapper.find('select').props().value).toBe(valueToSelect)

    expect(mockUseDispatch).toHaveBeenCalledTimes(1);
    expect(mockGetRestaurantsByCategory).toHaveBeenCalledTimes(1);

    expect(mockGetRestaurantsByCategory.mock.calls[0][0]).toBe(valueToSelect)
  });

  it("should not select a value from categoryFromParameter and don't dispatch get restaurants by a category with a empty value", () => {
    const valueToSelect = '';

    mockUseDispatch.mockReturnValue(jest.fn());
    mockGetRestaurantsByCategory.mockReturnValue(jest.fn());

    const wrapper = mount(
      <RestaurantCategory 
        loading={ false } 
        categories={ categoriesResponseJson.data as Category[] }
        categoryFromParameter={ valueToSelect }
      />
    );

    expect(wrapper.find('select').props().value).toBe(valueToSelect)

    expect(mockUseDispatch).toHaveBeenCalledTimes(0);
    expect(mockGetRestaurantsByCategory).toHaveBeenCalledTimes(0);
  });

  it("should not select a value from categoryFromParameter and don't dispatch get restaurants by a category with a invalid value", () => {
    const valueToSelect = 'italiana';

    mockUseDispatch.mockReturnValue(jest.fn());
    mockGetRestaurantsByCategory.mockReturnValue(jest.fn());

    const wrapper = mount(
      <RestaurantCategory 
        loading={ false } 
        categories={ categoriesResponseJson.data as Category[] }
        categoryFromParameter={ valueToSelect }
      />
    );

    expect(wrapper.find('select').props().value).toBe("")

    expect(mockUseDispatch).toHaveBeenCalledTimes(0);
    expect(mockGetRestaurantsByCategory).toHaveBeenCalledTimes(0);
  });
});