import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from "react-router-dom";

import { getRestaurantDetail } from '../../../gateway/actions/restaurantDetail.actions';
import { RestaurantDetailState } from "../../../gateway/reducers/restaurantDetail.reducer";
import { RootState } from "../../../gateway";
import RestaurantItem from './RestaurantItem';

const RestaurantOrder = () => {
  const { uuid } = useParams<{ uuid: string }>();

  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(getRestaurantDetail(uuid));
  }, [dispatch]);

  const { loading, restaurant, error }: RestaurantDetailState = useSelector((
    state: RootState
  ) => state.restaurantDetail);

  return (
    <>
      {!error && 
        <div className="container">
          <div className="row">
            <div className="card mb-3">
              <div className="row g-0">
                <div className="col-md-2">
                  <img src={ restaurant.logo } alt={ restaurant.name } style={{ maxWidth: "200px" }} />
                </div>
                <div className="col-md-10">
                  <div className="card-body">
                    <h5 className="card-title">{ restaurant.name }</h5>
                    <p className="card-text">{ restaurant.description }</p>
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
          {!loading && 
            restaurant.itens?.map(menuItem => {
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

export default RestaurantOrder;