import { useEffect, useState } from 'react';
import { useParams } from "react-router-dom";

import { fetchRestaurantDetail } from '../../../../services/Restaurant.services';
import { RestaurantDetail } from '../../../../services/RestaurantDetail.model';
import RestaurantItem from '../../components/RestaurantItem/RestaurantItem.component';

const Order = () => {
  const { uuid } = useParams<{ uuid: string }>();

  const [restaurant, setRestaurant] = useState<RestaurantDetail>();
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<boolean>(false);

  useEffect(() => {
    const getRestaurantDetail = async () => {
      try {
        let restaurantDetail = await fetchRestaurantDetail(uuid);
        setRestaurant(restaurantDetail);
      } catch(err: any) {
        setError(true); 
      }

      setLoading(false);
    }

    getRestaurantDetail();
  }, []);

  return (
    <>
      {!error && 
        <div className="container">
          <div className="row">
            <div className="card mb-3">
              <div className="row g-0">
                <div className="col-md-2">
                  <img src={ restaurant?.logo } alt={ restaurant?.name } style={{ maxWidth: "200px" }} />
                </div>
                <div className="col-md-10">
                  <div className="card-body">
                    <h5 className="card-title">{ restaurant?.name }</h5>
                    <p className="card-text">{ restaurant?.description }</p>
                    <p className="card-text"><small className="text-muted">TODO: Add stars from rating here!</small></p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      }
      <br />
      <div className="container">
        <div className="row">
          {!error && 
            restaurant?.itens?.map(menuItem => {
              return (
                <RestaurantItem 
                  uuid={ menuItem.uuid } 
                  name={ menuItem.name } 
                  description={ menuItem.description } 
                  value={ menuItem.value } 
                />
              );
            })
          }
        </div>
      </div>
    </>
  );
}

export default Order;