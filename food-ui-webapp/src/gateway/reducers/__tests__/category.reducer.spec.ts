// src/reducers/users.test.js
import { categoryReducer, CategoryState } from '../category.reducer';
import { 
  GET_CATEGORIES_LOADING, 
  GET_CATEGORIES_SUCCESS, 
  GET_CATEGORIES_FAILURE
} from '../../types';

import categoriesResponseJson from "../../../../dependencies/stubby/mocks/response/categories_response.json";

describe('category.reducer', () => {
  const initialState: CategoryState = {
    categories: [],
    error: false,
    loading: false
  };

  it('should returns the initial state when an action type is not passed', () => {
    const reducer = categoryReducer(initialState, <any>{});

    expect(reducer).toEqual(initialState);
  });

  it('should handles GET_CATEGORIES_LOADING as expected', () => {
    const reducer = categoryReducer(
      initialState, 
      <any>{ type: GET_CATEGORIES_LOADING }
    );

    expect(reducer).toEqual({
      categories: [],
      loading: true,
      error: false
    });
  });

  it('should handles GET_CATEGORIES_SUCCESS as expected', () => {
    const reducer = categoryReducer(
      initialState, 
      <any>{ 
        type: GET_CATEGORIES_SUCCESS, 
        payload: categoriesResponseJson.data
      }
    );

    expect(reducer).toEqual({
      categories: categoriesResponseJson.data,
      loading: false,
      error: false
    });
  });

  it('should handles GET_CATEGORIES_FAILURE as expected', () => {
    const reducer = categoryReducer(
      initialState, 
      <any>{ type: GET_CATEGORIES_FAILURE }
    );

    expect(reducer).toEqual({
      categories: [],
      loading: false,
      error: true
    });
  });
});