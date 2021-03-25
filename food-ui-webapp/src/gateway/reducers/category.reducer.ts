import { Reducer } from 'redux';

import { 
  GET_CATEGORIES_LOADING, 
  GET_CATEGORIES_SUCCESS, 
  GET_CATEGORIES_FAILURE, 
  CategoryActionTypes 
} from '../types';
import Category from '../../domain/Category';

export interface CategoryState {
  categories: Category[], 
  error: boolean, 
  loading: boolean
}

const initialState: CategoryState = {
  categories: [],
  error: false,
  loading: false
};

export const categoryReducer: Reducer<
  CategoryState,
  CategoryActionTypes
> = (state = initialState, action) => {
  switch (action.type) {
    case GET_CATEGORIES_LOADING: {
      return {
        ...state, 
        categories: [], 
        loading: action.loading, 
        error: false
      };
    }
    case GET_CATEGORIES_SUCCESS: {
      return {
        ...state,
        categories: action.payload, 
        loading: false, 
        error: false
      };
    }
    case GET_CATEGORIES_FAILURE: {
      return {
        ...state,
        categories: [], 
        loading: false, 
        error: true
      };
    }
    default:
      return state
  }
};