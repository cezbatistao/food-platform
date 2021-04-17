import React from "react";
import { useLocation } from "react-router-dom";

import Header from './components/header/Header';
import Notification from './components/notification/Notification';

type Props = {
}

const exclusionArray = [
  '/info',
  '/health',
]

const App: React.FC<Props> = (props) => {
  const location = useLocation();

  return (
    <>
      {exclusionArray.indexOf(location.pathname) < 0 && <Notification />}
      {exclusionArray.indexOf(location.pathname) < 0 && <Header/>}
      { props.children }
    </>
  );
}

export default App;