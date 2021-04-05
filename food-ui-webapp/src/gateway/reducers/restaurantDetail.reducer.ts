import { Reducer } from 'redux';

import { 
  GET_RESTAURANT_DETAIL_LOADING, 
  GET_RESTAURANT_DETAIL_SUCCESS, 
  GET_RESTAURANT_DETAIL_FAILURE, 
  RestaurantDetailActionTypes, 
} from '../types';
import RestaurantDetail from '../../domain/RestaurantDetail';
import MenuItem from '../../domain/MenuItem';

export interface RestaurantDetailState {
  restaurant: RestaurantDetail, 
  error: boolean, 
  loading: boolean
}

const initialState: RestaurantDetailState = {
  restaurant: <RestaurantDetail>{},
  error: false,
  loading: false
};

export const restaurantDetailReducer: Reducer<
RestaurantDetailState,
RestaurantDetailActionTypes
> = (state = initialState, action) => {
  switch (action.type) {
    case GET_RESTAURANT_DETAIL_LOADING: {
      return {
        ...state, 
        restaurant: <RestaurantDetail>{},
        loading: action.loading, 
        error: false
      };
    }
    case GET_RESTAURANT_DETAIL_SUCCESS: {
      return {
        ...state,
        restaurant: action.payload, 
        loading: false, 
        error: false
      };
    }
    case GET_RESTAURANT_DETAIL_FAILURE: {
      return {
        ...state,
        restaurant: <RestaurantDetail>{},
        loading: false, 
        error: true
      };
    }
    default:
      return state
  }
};