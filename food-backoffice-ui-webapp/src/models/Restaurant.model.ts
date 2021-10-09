import Category from "./Category.model";

export default interface Restaurant {
  uuid: string;
  name: string;
  category: Category;
  logo: string;
  description: string;
  address: string;
}

export default interface RestaurangPagging {
  restaurants: Restaurant[];
  pagging: Pagging
}

export interface Pagging {
  offset: number;
  limit: number;
  total: number;
}