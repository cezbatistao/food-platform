import { AxiosResponse } from 'axios';
import { axios } from './Api';

export interface CategoryResponse {
  id: number;
  code: string;
  description: string;
}

export interface RestaurantResponse {
  id: string;
  name: string;
  logo: string;
  description: string;
  address: string;
}

export const getAllCategories = async (): Promise< // change to fetchCategories
  CategoryResponse[]
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

  const AxiosResponse: AxiosResponse<CategoryResponse[]> = response?.data;
  return AxiosResponse?.data;
}

export const fetchRestaurantsByCategory = async (
  category: string
): Promise<RestaurantResponse[]> => {
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

  const AxiosResponse: AxiosResponse<RestaurantResponse[]> = response?.data;
  return AxiosResponse?.data;
}