import { Action } from "redux";

import RestaurantDetail from "../../domain/RestaurantDetail";

export const GET_RESTAURANT_DETAIL_LOADING = "GET_RESTAURANT_DETAIL_LOADING";
export const GET_RESTAURANT_DETAIL_SUCCESS = "GET_RESTAURANT_DETAIL_SUCCESS";
export const GET_RESTAURANT_DETAIL_FAILURE = "GET_RESTAURANT_DETAIL_FAILURE";

interface GetRestaurantDetailLoadingAction extends Action<typeof GET_RESTAURANT_DETAIL_LOADING> {
  loading: true, 
  error: false
}

interface GetRestaurantDetailAction extends Action<typeof GET_RESTAURANT_DETAIL_SUCCESS> {
  payload: RestaurantDetail, 
  loading: false, 
  error: false
}

interface GetRestaurantDetailFailureAction extends Action<typeof GET_RESTAURANT_DETAIL_FAILURE> {
  loading: false, 
  error: true
}

export type RestaurantDetailActionTypes = 
  | GetRestaurantDetailLoadingAction 
  | GetRestaurantDetailAction 
  | GetRestaurantDetailFailureAction;