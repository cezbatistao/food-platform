import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useLocation, useHistory } from "react-router-dom";
import queryString from 'query-string';
import { Location } from 'history';

import RestaurantCategory from './RestaurantCategory';
import Category from '../../../domain/Category';
import { RootState } from '../../../gateway';

import { getCategories } from '../../../gateway/actions/category.actions';

type CategoryState = {
  categories: Category[], 
  loading: boolean
}

const RestaurantCategoryContainer = () => {

  // const location = useLocation();
  // let history = useHistory();

  // const getCategoryFromQueryString = (location: Location<unknown>) => {
  //   const parsed = queryString.parse(location.search);
  //   return parsed.category ? parsed.category as string : undefined;
  // }

  // const handleChangeCategory = (event: React.ChangeEvent<{ value: unknown }>) => {
  //   const categorySelected = event.target.value as string;
  //   history.push({
  //     search: `?category=${categorySelected}`
  //   });
  // }

  // const categoryQueryString = getCategoryFromQueryString(location);

  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(getCategories());
    // dispatchGetRestaurantsByCategory(categoryQueryString as string);
    
  }, [dispatch]);

  const { loading, categories }: CategoryState = useSelector((
    state: RootState
  ) => state.category);

  // const dispatchGetRestaurantsByCategory = (category: string) => {
  //   if(category) {
  //     dispatch(getRestaurantsByCategory(category));
  //   }
  // }

  return (
    <RestaurantCategory 
      loading={loading} 
      categories={categories} 
      dispatch={dispatch} 
    />
  );
}

export default RestaurantCategoryContainer;