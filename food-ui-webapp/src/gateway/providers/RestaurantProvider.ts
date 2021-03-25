import { AxiosResponse } from 'axios';
import { axios } from './Api';

export interface ICategory { // TODO change to CategoryEntity
  id: number;
  code: string;
  description: string;
}

export interface IRestaurant { // TODO change to RestaurantEntity
  id: string;
  name: string;
  logo: string;
  description: string;
  address: string;
}

export const getAllCategories = async (): Promise< // change to fetchCategories
  ICategory[]
> => {
  // try {
  //   const response: AxiosResponse = await axios({
  //     url: 'http://localhost:8882/api/v1/categories', 
  //     method: 'get', 
  //     headers: {
  //       'Content-Type': 'application/json',
  //     }
  //   });

  //   const AxiosResponse: AxiosResponse<ICategory[]> = response.data;
  //   return AxiosResponse.data;
  // } catch (err) {
  //   console.error(err);
  // }

  const response: AxiosResponse = await axios({
    url: 'http://localhost:8882/api/v1/categories', 
    method: 'get', 
    headers: {
      'Content-Type': 'application/json',
    }
  });

  const AxiosResponse: AxiosResponse<ICategory[]> = response?.data;
  return AxiosResponse?.data;
}

export const fetchRestaurantsByCategory = async (
  category: string
): Promise<IRestaurant[]> => {
  const response: AxiosResponse = await axios({
    url: 'http://localhost:8882/api/v1/restaurants', 
    method: 'get', 
    headers: {
      'Content-Type': 'application/json',
    }, 
    params: {
      'category': category
    }
  });

  const AxiosResponse: AxiosResponse<IRestaurant[]> = response?.data;
  return AxiosResponse?.data;
}