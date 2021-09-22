import { createContext } from "react";

export type CategoryContextState = {
  category: string;
  setCategory: (name: string) => void;
};

const contextDefaultValue: CategoryContextState = {
  category: '', 
  setCategory: () => {}
};
  
export const CategoryContext = createContext<CategoryContextState>(
  contextDefaultValue
);

export default CategoryContext;