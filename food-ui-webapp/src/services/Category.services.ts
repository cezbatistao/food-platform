// import { axios } from './Api';
import axios, { AxiosResponse } from 'axios';

import Category from './Category.model';

export const fetchCategories = async (): Promise<
Category[]
> => {

  const response: AxiosResponse = await axios({
    url: '/food/restaurant/api/v1/categories', 
    method: 'get', 
    headers: {
      'Content-Type': 'application/json',
    }
  });

  const AxiosResponse: AxiosResponse<Category[]> = response?.data;
  return AxiosResponse?.data;
}