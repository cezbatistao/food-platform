import { Theme, createStyles, makeStyles, useTheme } from '@material-ui/core/styles';

import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';


import Header from "../../header/Header";
import Notification from "../../notification/Notification";

import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from "react-router-dom";

import { getRestaurantDetail } from '../../../gateway/actions/restaurantDetail.actions';
import { RestaurantDetailState } from "../../../gateway/reducers/restaurantDetail.reducer";
import { RootState } from "../../../gateway";
import RestaurantItem from './RestaurantItem';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      display: 'flex',
    },
    container: {
      paddingTop: theme.spacing(4),
      paddingBottom: theme.spacing(4),
    },
    details: {
      display: 'flex',
      flexDirection: 'column',
    },
    content: {
      flex: '1 0 auto',
    },
    cover: {
      width: 151,
    },
  }),
);

const RestaurantOrder = () => {
  const { uuid } = useParams<{ uuid: string }>();

  const classes = useStyles();
  const theme = useTheme();

  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(getRestaurantDetail(uuid));
  }, [dispatch]);

  const { loading, restaurant }: RestaurantDetailState = useSelector((
    state: RootState
  ) => state.restaurantDetail);

  return (
    <>
      <Notification />
      <Header />
      <Container maxWidth="lg" className={classes.container}>
        {!loading && 
          restaurant.itens?.map(menuItem => {
            return (
              <RestaurantItem 
                uuid={ menuItem.uuid } 
                name={ menuItem.name } 
                description={ menuItem.description } 
                value={ menuItem.value } 
              />
            );
          })
        }
      </Container>
    </>
  );
}

export default RestaurantOrder;