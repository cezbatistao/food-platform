import { useState, useEffect, useContext } from "react";

import RestaurantCard from "../../components/RestaurantCard/RestaurantCard.component";
import CategoryContext from '../../contexts/RestaurantCategory/RestaurantCategory.context';
import { fetchRestaurantsByCategory } from "../../../../services/Restaurant.services";
import Restaurant from "../../../../services/Restaurant.model";

const RestaurantList = () => {

  const { category } = useContext(CategoryContext);

  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    fetchRestaurantsByCategory(category)
      .then(restaurants => {
        setRestaurants(restaurants);
        setLoading(false);
      });
  }, [category]);

  return (
    <div key="restaurant-list" className="row row-cols-1 row-cols-md-5 g-4">
      {!loading &&
        restaurants.map(restaurant => {
          return(
            <RestaurantCard 
              key={ restaurant.uuid } 
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