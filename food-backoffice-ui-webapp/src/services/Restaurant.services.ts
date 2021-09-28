import axios from 'axios';

import Restaurant from '@/models/Restaurant.model';

export const fetchRestaurants = async (
): Promise<Restaurant[]> => {
  try {
    const response = await axios.get('/food/restaurant/api/v1/restaurants', {
      headers: {
        "Content-Type": "application/json"
      }
    });

    return response.data.data as Restaurant[];
  } catch(err: any) {
    const errorDomain: Error = err.response.data.error;
    throw errorDomain;
  }
}