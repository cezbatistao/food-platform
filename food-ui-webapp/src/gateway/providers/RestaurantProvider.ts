import { AxiosResponse } from 'axios';
import { axios } from './Api';

export interface CategoryResponse {
  uuid: string;
  code: string;
  description: string;
}

export interface RestaurantResponse {
  uuid: string;
  name: string;
  logo: string;
  description: string;
  address: string;
}

export interface MenuItemResponse {
  uuid: string;
  name: string;
  description: string;
  value: string;
}

export interface RestaurantDetailResponse {
  uuid: string;
  name: string;
  logo: string;
  description: string;
  address: string;
  itens: MenuItemResponse[]
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
debugger;
  const response: AxiosResponse = await axios({
    url: '/food/restaurant/api/v1/categories', 
    method: 'get', 
    headers: {
      'Content-Type': 'application/json',
    }
  });
debugger;
  const AxiosResponse: AxiosResponse<CategoryResponse[]> = response?.data;
  return AxiosResponse?.data;
}

export const fetchRestaurantsByCategory = async (
  category: string
): Promise<RestaurantResponse[]> => {
  const response: AxiosResponse = await axios({
    url: '/food/restaurant/api/v1/restaurants', 
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

export const fetchRestaurantDetail = async (
  uuid: string
): Promise<RestaurantDetailResponse> => {
  const response: AxiosResponse = await axios({
    url: `/food/restaurant/api/v1/restaurants/${uuid}`, 
    method: 'get', 
    headers: {
      'Content-Type': 'application/json',
    }
  });

  const AxiosResponse: AxiosResponse<RestaurantDetailResponse> = response?.data;
  return AxiosResponse?.data;
}
