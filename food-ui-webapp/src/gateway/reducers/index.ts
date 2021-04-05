import { combineReducers } from 'redux';

import { categoryReducer, CategoryState } from './category.reducer';
import { restaurantReducer, RestaurantState } from './restaurant.reducer';
import { restaurantDetailReducer, RestaurantDetailState } from './restaurantDetail.reducer';

export type AppState = {
  category: CategoryState, 
  restaurant: RestaurantState,
  restaurantDetail: RestaurantDetailState
};

export const rootReducer = combineReducers<AppState>({
  category: categoryReducer,
  restaurant: restaurantReducer,
  restaurantDetail: restaurantDetailReducer,
});

export type RootState = ReturnType<typeof rootReducer>;