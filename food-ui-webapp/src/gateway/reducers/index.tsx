import { combineReducers } from 'redux';

import { categoryReducer, CategoryState } from './category.reducer';
import { restaurantReducer, RestaurantState } from './restaurant.reducer';

export type AppState = {
  category: CategoryState, 
  restaurant: RestaurantState
};

export const rootReducer = combineReducers<AppState>({
  category: categoryReducer,
  restaurant: restaurantReducer,
});

export type RootState = ReturnType<typeof rootReducer>;