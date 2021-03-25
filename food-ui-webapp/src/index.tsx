import ReactDOM from 'react-dom';
import {
  Route, 
  BrowserRouter as Router, 
  Switch, 
  Redirect
} from 'react-router-dom';

import App from './App';
import Info from './components/Info/Info';
import Health from './components/Health/Health';

import RestaurantMain from './features/SearchRestaurant/screens/RestaurantMain/RestaurantMain.component';
import Order from './features/Order/screens/Order/Order.component';

ReactDOM.render(
  <Router forceRefresh={true}>
    <App>
      <Switch>
        <Route exact path="/" render={() => (<Redirect to="/home" />)} />
        <Route exact path="/home" component={RestaurantMain} />
        <Route exact path="/order/:uuid" component={Order} />
      </Switch>
    </App>
    <Switch>
      <Route exact path="/info" component={Info} />
      <Route exact path="/health" component={Health} />
    </Switch>
  </Router>,
  document.getElementById('root')
);