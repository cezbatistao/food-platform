import { useSelector } from 'react-redux';

import Grid from '@material-ui/core/Grid';

import RestaurantCard from './RestaurantCard';

import { RootState } from '../../gateway';
import Restaurant from '../../domain/Restaurant';

type RestaurantState = {
  restaurants: Restaurant[], 
  loading: boolean
}

const RestaurantList = () => {

  const { loading, restaurants }: RestaurantState = useSelector((
    state: RootState
  ) => state.restaurant);

  return (
    <Grid container spacing={3}>
      {
        restaurants.map(restaurant => {
          return(
            <Grid key={ restaurant.id } item xs={12} sm={4}>
              <RestaurantCard 
                name={ restaurant.name } 
                logo={ restaurant.logo }
                description={ restaurant.description }
                address={ restaurant.address }
              />
            </Grid>
          );
        })
      }
    </Grid>
  );
}

export default RestaurantList;