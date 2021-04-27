import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';

import Category from '../../../domain/Category';
import { getRestaurantsByCategory } from '../../../gateway/actions/restaurant.actions';

type Props = {
  loading: boolean,
  categories: Category[], 
  categoryFromParameter: string
}

const RestaurantCategory = ({ loading, categories, categoryFromParameter }: Props) => {
  
  const [categorySelected, setCategorySelected] = useState<string>(categoryFromParameter);
  const dispatch = useDispatch();

  useEffect(() => {
    setCategorySelected(categoryFromParameter);
    if(categorySelected && categories.some(category => category.code === categorySelected)) {
      dispatch(getRestaurantsByCategory(categorySelected));
    } else if(categorySelected && !categories.some(category => category.code === categorySelected)) {
      setCategorySelected("");
    }
  }, [categories]);

  const handleChangeCategory = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const categorySelectedFromSelect = event.target.value as string;
    setCategorySelected(categorySelectedFromSelect);
    dispatch(getRestaurantsByCategory(categorySelectedFromSelect));
    var refresh = `${window.location.protocol}//${window.location.host + window.location.pathname}?category=${categorySelectedFromSelect}`;
    window.history.pushState({ path: refresh }, '', refresh);
  }

  return (
    <select 
      data-test="select_category" 
      className="form-select" 
      aria-label="Selecione uma categoria" 
      onChange={ handleChangeCategory }
      value= { categorySelected }
    >
      <option value="" disabled>Selecione uma categoria</option>
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