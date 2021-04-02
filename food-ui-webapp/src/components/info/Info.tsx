import JSONPretty from 'react-json-pretty';

import { Config } from '../../config';

const Info = () => {

  const infoData = {
    app: {
      group: "com.food",
      name: process.env.REACT_APP_NAME,
      description: `${process.env.REACT_APP_DESCRIPTION}`, 
      version: `${process.env.REACT_APP_VERSION}`, 
      environment: `${Config.environment_app}`
    }
  };

  return <JSONPretty id="json-pretty" data={infoData}></JSONPretty>;
}

export default Info;