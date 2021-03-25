import React from 'react';

import Header from './components/header/Header';
import Main from './components/main/Main';

import './App.css';
import Notification from './components/notification/Notification';

const App: React.FC = (): JSX.Element => {
  return (
    <>
      <Notification />
      <Header />
      <Main />
    </>
  );
};

export default App;