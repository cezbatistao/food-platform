import { ActionCreator, AnyAction } from 'redux';
import { ThunkAction, ThunkDispatch } from 'redux-thunk';

import { GET_CATEGORIES_LOADING, 
  GET_CATEGORIES_SUCCESS, 
  GET_CATEGORIES_FAILURE, 
  CategoryActionTypes, 
} from '../types';
import Category from '../../domain/Category';
import { 
  getAllCategories, 
  CategoryResponse 
} from '../providers/RestaurantProvider';

const getCategoriesLoading: ActionCreator<
  CategoryActionTypes
> = () => {
  return { 
    type: GET_CATEGORIES_LOADING, 
    loading: true,
    error: false
  };
}

const getCategoriesSuccess: ActionCreator<CategoryActionTypes> = (
  categories: Category[]
) => {
  return { 
    type: GET_CATEGORIES_SUCCESS, 
    payload: categories, 
    loading: false,
    error: false
  };
}

const getCategoriesFailure: ActionCreator<
  CategoryActionTypes
> = () => {
  return { 
    type: GET_CATEGORIES_FAILURE, 
    loading: false,
    error: true
  };
}

export const getCategories = (): ThunkAction<
  Promise<CategoryActionTypes>, Category[], null, AnyAction
> => {
  return async (
    dispatch: ThunkDispatch<Category[], null, AnyAction>
  ): Promise<CategoryActionTypes> => {
    dispatch(getCategoriesLoading());

    const response = await getAllCategories();

    // if(response == null || response.error) {
    if(response == null) {
      return dispatch(getCategoriesFailure());
    } 
    
    const categoriesResponse: CategoryResponse[] = response;
    const categories = categoriesResponse.map(categoryResponse => {
      return new Category(
        categoryResponse.uuid, 
        categoryResponse.code, 
        categoryResponse.description
      );
    });

    return dispatch(getCategoriesSuccess(categories));
  }
}