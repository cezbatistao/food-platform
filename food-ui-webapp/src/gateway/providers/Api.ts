import axios, { AxiosResponse, AxiosError } from 'axios';

import { showWarning, showError } from '../actions/notification';
import Error from '../../domain/Error';

const errorResponseHandler = (error: AxiosError) => {
  if (error.response) {
    console.log(error.response.data);
    console.log(error.response.status);
    console.log(error.response.headers);
    showError("Ocorreu um erro na aplicação, verifique com o suporte.");
  } else if (error.request) {
    // error.response.status
    console.error(error.request);
    const errorDomain: Error = error.request.data.error;

    const httpStatus: number = error.request.status;
    if(httpStatus >= 400 && httpStatus < 500) {
      showWarning(errorDomain.message);
    } else if(httpStatus >= 500) {
      showError(errorDomain.message);
    }
  } else {
    console.log('Error', error.message);
    showError("Ocorreu um erro na aplicação, verifique com o suporte.");
  }
}

// apply interceptor on response
axios.interceptors.response.use(
 response => response,
 errorResponseHandler
);

export { axios };