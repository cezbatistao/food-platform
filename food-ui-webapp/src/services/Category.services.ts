import axios, { AxiosResponse } from 'axios';

import Category from './Category.model';

export const fetchCategories = async (): Promise<
  Category[]
> => {
  try {
    const response = await axios.get('/food/restaurant/api/v1/categories', {
      headers: {
        "Content-Type": "application/json"
      }
    });

    return response.data.data as Category[];
  } catch(err: any) {
    const errorDomain: Error = err.response.data.error;
    throw errorDomain;
  }
}