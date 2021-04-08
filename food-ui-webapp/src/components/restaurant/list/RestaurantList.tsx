import Grid from '@material-ui/core/Grid';

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
    <Grid container spacing={3}>
      {
        restaurants.map(restaurant => {
          return(
            <Fragment key={ restaurant.uuid }>
              <RestaurantCard 
                uuid={ restaurant.uuid } 
                name={ restaurant.name } 
                logo={ restaurant.logo }
                description={ restaurant.description }
                address={ restaurant.address }
              />
            </Fragment>
          );
        })
      }
    </Grid>
  );
}

export default RestaurantList;