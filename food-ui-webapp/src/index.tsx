import './index.scss';

import React from 'react';
import ReactDOM from 'react-dom';
import {Route, BrowserRouter as Router, Switch} from 'react-router-dom';
import { Provider } from 'react-redux'

import App from './App';
import RestaurantOrder from './components/restaurant/order/RestaurantOrder';
import Info from './components/info/Info';
import Health from './components/health/Health';

import { store } from './gateway'

ReactDOM.render(
  <React.StrictMode>
    <Provider store={store}>
    <Router>
        <Switch>
          <Route exact path="/" component={App} />
          <Route exact path="/order/:uuid" component={RestaurantOrder} />
          <Route exact path="/info" component={Info} />
          <Route exact path="/health" component={Health} />
        </Switch>
      </Router>
    </Provider>
  </React.StrictMode>,
  document.getElementById('root')
);