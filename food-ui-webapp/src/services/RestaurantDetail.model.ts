export interface MenuItem {
    uuid: string;
    name: string;
    description: string;
    value: string;
  }
  
  export interface RestaurantDetail {
    uuid: string;
    name: string;
    logo: string;
    description: string;
    address: string;
    itens: MenuItem[]
  }