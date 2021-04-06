import './index.scss';

import ReactDOM from 'react-dom';
import {
  Route, 
  BrowserRouter as Router, 
  Switch, 
  Redirect
} from 'react-router-dom';
import { Provider } from 'react-redux'

import App from './App';
import Main from './components/main/Main';
import RestaurantOrder from './components/restaurant/order/RestaurantOrder';
import Info from './components/info/Info';
import Health from './components/health/Health';

import { store } from './gateway'

ReactDOM.render(
  <Provider store={store}>
    <Router forceRefresh={true}>
      <App>
        <Switch>
          <Route exact path="/" render={() => (<Redirect to="/home" />)} />
          <Route exact path="/home" component={Main} />
          <Route exact path="/order/:uuid" component={RestaurantOrder} />
          <Route exact path="/info" component={Info} />
          <Route exact path="/health" component={Health} />
        </Switch>
      </App>
    </Router>
  </Provider>,
  document.getElementById('root')
);