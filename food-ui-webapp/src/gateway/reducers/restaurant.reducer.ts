import { Reducer } from 'redux';

import { 
  GET_RESTAURANTS_LOADING, 
  GET_RESTAURANTS_SUCCESS, 
  GET_RESTAURANTS_FAILURE, 
  RestaurantActionTypes, 
} from '../types';
import Restaurant from '../../domain/Restaurant';

export interface RestaurantState {
  restaurants: Restaurant[], 
  error: boolean, 
  loading: boolean
}

const initialState: RestaurantState = {
  restaurants: [],
  error: false,
  loading: false
};

export const restaurantReducer: Reducer<
  RestaurantState,
  RestaurantActionTypes
> = (state = initialState, action) => {
  switch (action.type) {
    case GET_RESTAURANTS_LOADING: {
      return {
        ...state, 
        restaurants: [],
        loading: action.loading, 
        error: false
      };
    }
    case GET_RESTAURANTS_SUCCESS: {
      return {
        ...state,
        restaurants: action.payload, 
        loading: false, 
        error: false
      };
    }
    case GET_RESTAURANTS_FAILURE: {
      return {
        ...state,
        restaurants: [],
        loading: false, 
        error: true
      };
    }
    default:
      return state
  }
};