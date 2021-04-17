import { Fragment } from 'react';
import { useSelector } from 'react-redux';

import RestaurantCard from '../card/RestaurantCard';
import { RootState } from '../../../gateway';
import Restaurant from '../../../domain/Restaurant';

type RestaurantState = {
  restaurants: Restaurant[], 
  loading: boolean
}

const RestaurantList = () => {

  const { loading, restaurants }: RestaurantState = useSelector((
    state: RootState
  ) => state.restaurant);
  
  return (
    <div className="row row-cols-1 row-cols-md-5 g-4">
      {
        restaurants.map(restaurant => {
          return(
            <RestaurantCard 
              uuid={ restaurant.uuid } 
              name={ restaurant.name } 
              logo={ restaurant.logo }
              description={ restaurant.description }
              address={ restaurant.address }
            />
          );
        })
      }
    </div>
  );
}

export default RestaurantList;