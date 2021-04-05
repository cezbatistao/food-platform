import { ActionCreator, AnyAction } from 'redux';
import { ThunkAction, ThunkDispatch } from 'redux-thunk';

import { 
  GET_RESTAURANTS_LOADING, 
  GET_RESTAURANTS_SUCCESS, 
  GET_RESTAURANTS_FAILURE, 
  RestaurantActionTypes, 
} from '../types';
import Restaurant from '../../domain/Restaurant';
import { 
  fetchRestaurantsByCategory, 
  RestaurantResponse 
} from '../providers/RestaurantProvider';

const getRestaurantsByCategoryLoading: ActionCreator<
  RestaurantActionTypes
> = () => {
  return { 
    type: GET_RESTAURANTS_LOADING, 
    loading: true,
    error: false
  };
}

const getRestaurantsByCategorySuccess: ActionCreator<RestaurantActionTypes> = (
  restaurants: Restaurant[]
) => {
  return { 
    type: GET_RESTAURANTS_SUCCESS, 
    payload: restaurants, 
    loading: false,
    error: false
  };
}

const getRestaurantsByCategoryFailure: ActionCreator<
  RestaurantActionTypes
> = () => {
  return { 
    type: GET_RESTAURANTS_FAILURE, 
    loading: false,
    error: true
  };
}

export const getRestaurantsByCategory = (category: string): ThunkAction<
  Promise<RestaurantActionTypes>, Restaurant[], null, AnyAction
> => {
  return async (
    dispatch: ThunkDispatch<Restaurant[], null, AnyAction>
  ): Promise<RestaurantActionTypes> => {
    dispatch(getRestaurantsByCategoryLoading());

    const response = await fetchRestaurantsByCategory(category);

    if(!response) {
      return dispatch(getRestaurantsByCategoryFailure());
    }
    
    const restaurantsResponse: RestaurantResponse[] = response;
    const restaurants = restaurantsResponse.map(restaurantResponse => {
      return new Restaurant(
        restaurantResponse.uuid, 
        restaurantResponse.name,
        restaurantResponse.logo,
        restaurantResponse.description,
        restaurantResponse.address
      );
    });

    return dispatch(getRestaurantsByCategorySuccess(restaurants));
  }
}