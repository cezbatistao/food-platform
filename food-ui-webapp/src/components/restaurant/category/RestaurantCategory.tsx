import { useDispatch } from 'react-redux';

import Category from '../../../domain/Category';
import { getRestaurantsByCategory } from '../../../gateway/actions/restaurant.actions';

type Props = {
  loading: boolean,
  categories: Category[]
}

const RestaurantCategory = ({ loading, categories }: Props) => {
  
  const dispatch = useDispatch();

  const handleChangeCategory = (event: React.ChangeEvent<HTMLSelectElement>) => {
    // event.preventDefault();
    const categorySelected = event.target.value as string;
    dispatch(getRestaurantsByCategory(categorySelected));
  }

  return (
    <select className="form-select" aria-label="Default select example" onChange={ handleChangeCategory }>
      <option disabled selected>Selecione uma categoria</option>
      {!loading && 
        categories.map(category => {
          return (
            <option 
              key={ category.uuid } 
              value={ category.code }
            >
              { category.description }
            </option>
          );
        })
      }
    </select>
  );
}

export default RestaurantCategory;