import { useHistory } from 'react-router-dom';

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
      key={ uuid } 
      onClick={ () => handleClickCard(uuid) } 
      className="col"
      style={{cursor:'pointer'}} 
    >
      <div data-test="card_header_restaurant_data" className="card h-100">
        <div className="card-header bg-transparent"><h5>{ name }</h5></div>
        <img src={ logo } className="card-img-top" alt={ name } />
        <div className="card-body">
          <h6 className="card-title">{ address }</h6>
          <p className="card-text"></p>
          <div className="card-footer bg-transparent">TODO: Add stars from rating here!</div>
        </div>
      </div>
    </div>
  );
}

export default RestaurantCard;