import { FunctionComponent } from 'react';

import Header from './components/header/Header';
import Notification from './components/notification/Notification';

import './App.scss';

type Props = {
}

const App: FunctionComponent<Props> = (props) => <>
  <Notification />
  <Header />
  { props.children }
</>

export default App;