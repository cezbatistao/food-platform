import JSONPretty from 'react-json-pretty';

const Health = () => {

  const healthData = {
    status: "UP"
  };

  return <JSONPretty id="json-pretty" data={healthData}></JSONPretty>;
}

export default Health;