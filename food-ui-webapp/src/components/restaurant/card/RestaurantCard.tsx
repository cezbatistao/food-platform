import { useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';

import Restaurant from '../../../domain/Restaurant';
import { RootState } from '../../../gateway';

type Props = {
  uuid: string,
  name: string,
  logo: string, 
  description: string,
  address: string
}

const RestaurantCard = ({uuid, name, logo, description, address}: Props) => {

  let history = useHistory();

  const handleClickCard = (uuidRestaurant: string) => {
    history.push(`/order/${uuidRestaurant}`);
  }

  return (
    <div 
      onClick={ () => handleClickCard(uuid) } 
      key={ uuid } 
      className="col"
    >
      <div className="card h-100">
        <div className="card-header bg-transparent"><h5>{ name }</h5></div>
        <img src={ logo } className="card-img-top" alt={ name } />
        <div className="card-body">
          <h6 className="card-title">{ address }</h6>
          <p className="card-text"></p>
          <div className="card-footer bg-transparent">TODO: Add stars from rating here!</div>
          <a href="#" className="stretched-link"></a>
        </div>
      </div>
    </div>
  );
}

export default RestaurantCard;