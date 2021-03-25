import { ActionCreator, AnyAction } from 'redux';
import { ThunkAction, ThunkDispatch } from 'redux-thunk';

import { 
  GET_RESTAURANTS_LOADING, 
  GET_RESTAURANTS_SUCCESS, 
  GET_RESTAURANTS_FAILURE, 
  RestaurantActionTypes, 
} from '../types';
import Restaurant from '../../domain/Restaurant';
import { fetchRestaurantsByCategory, IRestaurant } from '../providers/RestaurantProvider';

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
  categories: Restaurant[]
) => {
  return { 
    type: GET_RESTAURANTS_SUCCESS, 
    payload: categories, 
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
    
    const restaurantsEntity: IRestaurant[] = response; // IRestaurant to Restaurant
    const restaurants = restaurantsEntity.map(restaurantEntity => {
      return new Restaurant(
        restaurantEntity.id, 
        restaurantEntity.name,
        restaurantEntity.logo,
        restaurantEntity.description,
        restaurantEntity.address
      );
    });

    return dispatch(getRestaurantsByCategorySuccess(restaurants));
  }
}