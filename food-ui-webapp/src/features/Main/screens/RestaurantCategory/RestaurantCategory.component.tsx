import { useEffect, useState, useContext } from 'react';
import { useLocation } from "react-router-dom";
import queryString from 'query-string';
import { Location } from 'history';

import { fetchCategories } from '../../../../services/Category.services';
import CategoryContext from '../../contexts/RestaurantCategory/RestaurantCategory.context';
import Category from '../../../../services/Category.model';

const RestaurantCategory = () => {

  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  const { category, setCategory } = useContext(CategoryContext);

  const location = useLocation();

  useEffect(() => {
    fetchCategories()
      .then(categories => {
        setCategories(categories);
        setLoading(false);
      });

    const categoryQueryString = getCategoryFromQueryString(location);
    setCategory(categoryQueryString);
  }, [location]);

  const getCategoryFromQueryString = (location: Location<unknown>) => {
    const parsed = queryString.parse(location.search);
    return parsed.category ? parsed.category as string : "";
  }

  const handleChangeCategory = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const categorySelected = event.target.value as string;
    setCategory(categorySelected);
    var refresh = `${window.location.protocol}//${window.location.host + window.location.pathname}?category=${categorySelected}`;
    window.history.pushState({ path: refresh }, '', refresh);
  }

  return (
    <select 
      data-testid="select-category" 
      className="form-select" 
      aria-label="Selecione uma categoria" 
      onChange={ handleChangeCategory }
      defaultValue={ category }
    >
      <option data-testid="no-category" value="" disabled>Selecione uma categoria</option>
      {!loading && 
        categories.map(category => {
          return (
            <option 
              data-testid={ category.code } 
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