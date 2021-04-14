import { ActionCreator, AnyAction } from 'redux';
import { ThunkAction, ThunkDispatch } from 'redux-thunk';

import { 
  GET_RESTAURANT_DETAIL_LOADING, 
  GET_RESTAURANT_DETAIL_SUCCESS, 
  GET_RESTAURANT_DETAIL_FAILURE, 
  RestaurantDetailActionTypes, 
} from '../types';
import RestaurantDetail from '../../domain/RestaurantDetail';
import MenuItem from '../../domain/MenuItem';
import { 
  fetchRestaurantDetail, 
  RestaurantDetailResponse
} from '../providers/RestaurantProvider';

const getRestaurantDetailLoading: ActionCreator<
  RestaurantDetailActionTypes
> = () => {
  return { 
    type: GET_RESTAURANT_DETAIL_LOADING, 
    loading: true,
    error: false
  };
}

const getRestaurantDetailSuccess: ActionCreator<RestaurantDetailActionTypes> = (
  restaurant: RestaurantDetail
) => {
  return { 
    type: GET_RESTAURANT_DETAIL_SUCCESS, 
    payload: restaurant, 
    loading: false,
    error: false
  };
}

const getRestaurantDetailFailure: ActionCreator<
  RestaurantDetailActionTypes
> = () => {
  return { 
    type: GET_RESTAURANT_DETAIL_FAILURE, 
    loading: false,
    error: true
  };
}

export const getRestaurantDetail = (uuid: string): ThunkAction<
  Promise<RestaurantDetailActionTypes>, RestaurantDetail, null, AnyAction
> => {
  return async (
    dispatch: ThunkDispatch<RestaurantDetail, null, AnyAction>
  ): Promise<RestaurantDetailActionTypes> => {
    
    dispatch(getRestaurantDetailLoading());

    const response = await fetchRestaurantDetail(uuid);

    if(!response) {
      return dispatch(getRestaurantDetailFailure());
    }
    
    const restaurantDetailResponse: RestaurantDetailResponse = response;

    const restaurantDetail = new RestaurantDetail(
      restaurantDetailResponse.uuid, 
      restaurantDetailResponse.name,
      restaurantDetailResponse.logo,
      restaurantDetailResponse.description,
      restaurantDetailResponse.address, 
      restaurantDetailResponse.itens.map(menuItemResponse => {
        return new MenuItem(
          menuItemResponse.uuid, 
          menuItemResponse.name, 
          menuItemResponse.description, 
          menuItemResponse.value
        );
      })
    )

    return dispatch(getRestaurantDetailSuccess(restaurantDetail));
  }
}