import { makeStyles } from '@material-ui/core/styles';

import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';

import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import Category from '../../domain/Category';
import { getCategories } from '../../gateway/actions/category.actions';
import { getRestaurantsByCategory } from '../../gateway/actions/restaurant.actions';
import { RootState } from '../../gateway';

const useStyles = makeStyles((theme) => ({
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
  paper: {
    padding: theme.spacing(2),
    display: 'flex',
    overflow: 'auto',
    flexDirection: 'column',
  },
  fixedHeight: {
    height: 240,
  }
}));

type CategoryState = {
  categories: Category[], 
  loading: boolean
}

const RestaurantCategory = () => {

  const handleChangeCategory = (event: React.ChangeEvent<{ value: unknown }>) => {
    const categorySelected = event.target.value as string;
    dispatch(getRestaurantsByCategory(categorySelected));
  }

  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(getCategories());
  }, [dispatch]);

  const { loading, categories }: CategoryState = useSelector((
    state: RootState
  ) => state.category);

  const classes = useStyles();

  return (
    <div>
      <FormControl className={classes.formControl}>
        <InputLabel id="demo-simple-select-label">Categoria</InputLabel>
        <Select 
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          onChange={handleChangeCategory}
        >
          {!loading && 
            categories.map(category => {
              return <MenuItem key={ category.id } value={ category.id }>{ category.description }</MenuItem>;
            })
          }
        </Select>
      </FormControl>
    </div>
  );
}

export default RestaurantCategory;