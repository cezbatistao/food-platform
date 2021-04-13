import { makeStyles } from '@material-ui/core/styles';

import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';

import { useDispatch } from 'react-redux';

import Category from '../../../domain/Category';
import { getRestaurantsByCategory } from '../../../gateway/actions/restaurant.actions';

const useStyles = makeStyles((theme) => ({
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
}));

type Props = {
  loading: boolean,
  categories: Category[]
}

const RestaurantCategory = ({ loading, categories }: Props) => {
  
  const classes = useStyles();

  const dispatch = useDispatch();

  const handleChangeCategory = (event: React.ChangeEvent<{ value: unknown }>) => {
    const categorySelected = event.target.value as string;
    dispatch(getRestaurantsByCategory(categorySelected));
  }

  return (
    <FormControl className={classes.formControl}>
      <InputLabel id="demo-simple-select-label">Categoria</InputLabel>
      <Select 
        data-test="select_category"
        labelId="demo-simple-select-label"
        id="demo-simple-select"
        onChange={handleChangeCategory}
      >
        {!loading && 
          categories.map(category => {
            return <MenuItem key={ category.uuid } value={ category.code }>{ category.description }</MenuItem>;
          })
        }
      </Select>
    </FormControl>
  );
}

export default RestaurantCategory;