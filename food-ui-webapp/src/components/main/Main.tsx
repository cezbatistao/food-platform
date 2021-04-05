import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';

import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';

import RestaurantList from '../restaurant/list/RestaurantList';
import RestaurantCategory from '../restaurant/category/RestaurantCategory';

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  rootContainer: {
    flexGrow: 1,
  },
  container: {
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(4),
  },
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
  },
}));

const Main = () => {

  const classes = useStyles();
  const fixedHeightPaper = clsx(classes.paper);

  return (
    <Container maxWidth="lg" className={classes.container}>
      <Grid container spacing={3}>
        <Grid item xs={12} md={8} lg={9}>
          <Paper className={fixedHeightPaper}>
            <RestaurantCategory />
          </Paper>
        </Grid>
      </Grid>
      <Grid container spacing={3}>
        <Grid item xs={12} lg={9}>
          <Paper className={fixedHeightPaper}>
            <RestaurantList />
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
}

export default Main;