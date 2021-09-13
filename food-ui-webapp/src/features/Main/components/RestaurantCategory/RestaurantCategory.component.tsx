import { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useLocation, useHistory } from "react-router-dom";
import queryString from 'query-string';
import { Location } from 'history';

import { fetchCategories } from '../../../../services/Category.services';
import CategoryContext from '../../contexts/RestaurantCategory/RestaurantCategory.context';
import Category from '../../../../services/Category.model';

const RestaurantCategory = () => {

  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  const { setCategory } = useContext(CategoryContext);

  useEffect(() => {
    const getCategories = async () => {
      const allCategories: Category[] = await fetchCategories();
      setCategories(allCategories);
      setLoading(false);
    }

    setLoading(true);
    getCategories();
  }, []);

  const handleChangeCategory = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const categorySelectedFromSelect = event.target.value as string;
    setCategory(categorySelectedFromSelect)
    // setCategorySelected(categorySelectedFromSelect);
    // dispatch(getRestaurantsByCategory(categorySelectedFromSelect));
    // var refresh = `${window.location.protocol}//${window.location.host + window.location.pathname}?category=${categorySelectedFromSelect}`;
    // window.history.pushState({ path: refresh }, '', refresh);
  }

  return (
    <select 
      data-test="select_category" 
      className="form-select" 
      aria-label="Selecione uma categoria" 
      onChange={ handleChangeCategory }
      defaultValue=''
      // value= { categorySelected }
    >
      <option value='' disabled selected>Selecione uma categoria</option>
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