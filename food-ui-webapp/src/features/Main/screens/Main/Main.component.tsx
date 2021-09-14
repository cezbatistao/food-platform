import RestaurantCategory from "../RestaurantCategory/RestaurantCategory.component";
import RestaurantList from '../RestaurantList/RestaurantList.component';
import CategoryContext from '../../contexts/RestaurantCategory/RestaurantCategory.context';

import { useState } from "react";

const Main = () => {

  const [category, setCategory] = useState('');

  return (
    <CategoryContext.Provider value={{ category, setCategory }}>
      <div className="container">
        <div className="row">
          <div className="col-6 col-md-4">
            <RestaurantCategory />
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
  );
}

export default Main;