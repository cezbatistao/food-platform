import RestaurantList from '../restaurant/list/RestaurantList';
import RestaurantCategoryContainer from '../restaurant/category/RestaurantCategoryContainer';

const Main = () => {

  return (
    <>
      <div className="container">
        <div className="row">
          <div className="col-6 col-md-4">
            <RestaurantCategoryContainer />
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
    </>
  );
}

export default Main;