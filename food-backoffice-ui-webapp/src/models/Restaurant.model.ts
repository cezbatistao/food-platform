import Category from "./Category.model";

export default interface Restaurant {
  uuid: string;
  name: string;
  category: Category;
  logo: string;
  description: string;
  address: string;
}