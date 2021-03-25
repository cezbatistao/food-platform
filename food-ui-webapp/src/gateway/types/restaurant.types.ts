import { Action } from "redux";

import Restaurant from "../../domain/Restaurant";

export const GET_RESTAURANTS_LOADING = "GET_RESTAURANTS_LOADING";
export const GET_RESTAURANTS_SUCCESS = "GET_RESTAURANTS_SUCCESS";
export const GET_RESTAURANTS_FAILURE = "GET_RESTAURANTS_FAILURE";

interface GetRestaurantsLoadingAction extends Action<typeof GET_RESTAURANTS_LOADING> {
  loading: true, 
  error: false
}

interface GetRestaurantsSuccessAction extends Action<typeof GET_RESTAURANTS_SUCCESS> {
  payload: Restaurant[], 
  loading: false, 
  error: false
}

interface GetRestaurantsFailureAction extends Action<typeof GET_RESTAURANTS_FAILURE> {
  loading: false, 
  error: true
}

export type RestaurantActionTypes = 
  | GetRestaurantsLoadingAction 
  | GetRestaurantsSuccessAction 
  | GetRestaurantsFailureAction;