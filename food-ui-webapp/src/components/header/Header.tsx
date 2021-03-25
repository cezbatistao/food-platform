import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import { Fastfood, ShoppingBasket } from '@material-ui/icons';

import orange from '@material-ui/core/colors/orange';

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

const Header: React.FC = (): JSX.Element => {
  
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <AppBar className={classes.appBarColor} style={{background: 'linear-gradient(45deg, #ffa000 30%, #ef6c00 90%)'}} position="static">
        <Toolbar>
          <Fastfood className={classes.menuButton} />
          <Typography variant="h6" className={classes.title}>
            Food Platform
          </Typography>
          <ShoppingBasket />
        </Toolbar>
      </AppBar>
    </div>
  );
}

export default Header;