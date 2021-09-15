type Props = {
  uuid: string,
  name: string,
  description: string,
  value: string
}

const RestaurantItem = ({uuid, name, description, value}: Props) => {
  
  const formatNumber = (valueStr: string) => {
    var value: number = +valueStr;
    return value.toLocaleString('pt-br', {style: 'currency', currency: 'BRL'});
  }
  
  return (
    <div key={ uuid } className="card mb-3">
      <div className="row g-0">
        <div className="col-md-2">
          <img src="https://astron.com.br/wp-content/uploads/2017/07/pizza-site-or.jpg" alt="..." style={{ maxWidth: "200px" }} />
        </div>
        <div className="col-md-10">
          <div className="card-body">
            <h5 className="card-title">{ name }</h5>
            <p className="card-text">{ description }</p>
            <p className="card-text"><small className="text-muted">{ formatNumber(value) }</small></p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default RestaurantItem;