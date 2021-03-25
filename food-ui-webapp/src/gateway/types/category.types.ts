import { Action } from "redux";

import Category from "../../domain/Category";

export const GET_CATEGORIES_LOADING = "GET_CATEGORIES_LOADING";
export const GET_CATEGORIES_SUCCESS = "GET_CATEGORIES_SUCCESS";
export const GET_CATEGORIES_FAILURE = "GET_CATEGORIES_FAILURE";

interface GetCategoriesLoadingAction extends Action<typeof GET_CATEGORIES_LOADING> {
  loading: true, 
  error: false
}

interface GetCategoriesSuccessAction extends Action<typeof GET_CATEGORIES_SUCCESS> {
  payload: Category[], 
  loading: false, 
  error: false
}

interface GetCategoriesFailureAction extends Action<typeof GET_CATEGORIES_FAILURE> {
  loading: false, 
  error: true
}

export type CategoryActionTypes = 
  | GetCategoriesLoadingAction 
  | GetCategoriesSuccessAction 
  | GetCategoriesFailureAction;