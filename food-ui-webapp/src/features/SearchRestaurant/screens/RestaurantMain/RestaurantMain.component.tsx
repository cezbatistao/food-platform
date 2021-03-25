import { useState, Fragment } from "react";

import RestaurantFilter from "../RestaurantFilter/RestaurantFilter.component";
import RestaurantList from '../RestaurantList/RestaurantList.component';
import CategoryContext from '../../contexts/RestaurantCategory/RestaurantCategory.context';

const RestaurantMain = () => {

  const [category, setCategory] = useState('');

  return (
    <div data-testid="restaurant-main">
      <CategoryContext.Provider value={{ category, setCategory }}>
        <div className="container">
          <div className="row">
            <div className="col-6 col-md-4">
              <RestaurantFilter />
            </div>
          </div>
        </div>
        <br />
        <div className="container">
          <div className="row">
            <div className="col-8 col-md-12">
              <RestaurantList />
            </div>
          </div>
        </div>
      </CategoryContext.Provider>
    </div>
  );
}

export default RestaurantMain;