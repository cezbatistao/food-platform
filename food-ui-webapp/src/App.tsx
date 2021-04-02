import Header from './components/header/Header';
import Main from './components/main/Main';

import './App.scss';
import Notification from './components/notification/Notification';

const App = () => {
  return (
    <>
      <Notification />
      <Header />
      <Main />
    </>
  );
};

export default App;