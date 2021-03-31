import { makeStyles } from '@material-ui/core/styles';
import orange from '@material-ui/core/colors/orange';

import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import { Fastfood, ShoppingBasket } from '@material-ui/icons';

import { Config } from '../../config';

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
  },
  appBarColor: {
    backgroundColor: orange['800'],
  }
}));

const Header = () => {
  
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <AppBar className={classes.appBarColor} style={{background: 'linear-gradient(45deg, #ffa000 30%, #ef6c00 90%)'}} position="static">
        <Toolbar>
          <Fastfood className={classes.menuButton} />
          <Typography variant="h6" className={classes.title}>
            Food Platform - {Config.environment_app}
          </Typography>
          <ShoppingBasket />
        </Toolbar>
      </AppBar>
    </div>
  );
}

export default Header;