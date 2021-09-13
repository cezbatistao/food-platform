// import { axios } from './Api';
import axios, { AxiosResponse } from 'axios';
import { showError, showWarning } from './Notification.service';
import Restaurant from './Restaurant.model';

import { RestaurantDetail } from './RestaurantDetail.model';

export const fetchRestaurantsByCategory = async (
  category: string
): Promise<Restaurant[]> => {
  try {
    const response = await axios.get('/food/restaurant/api/v1/restaurants', {
      headers: {
        "Content-Type": "application/json"
      }, 
      params: {
        category: category
      }
    });

    return response.data.data as Restaurant[];
  } catch(err: any) {
    const errorDomain: Error = err.response.data.error;
    throw errorDomain;
  }
}

export const fetchRestaurantDetail = async (
  uuid: string
): Promise<RestaurantDetail> => {
  try {
    const response = await axios.get(`/food/restaurant/api/v1/restaurants/${uuid}`, {
      headers: {
        "Content-Type": "application/json"
      }
    });

    return response.data.data as RestaurantDetail;
  } catch(err: any) {
    const errorDomain: Error = err.response.data.error;

    const httpStatus: number = err.response.status;
    if(httpStatus >= 400 && httpStatus < 500) {
      showWarning(errorDomain.message);
    } else if(httpStatus >= 500) {
      showError(errorDomain.message);
    }

    throw errorDomain;
  }
}