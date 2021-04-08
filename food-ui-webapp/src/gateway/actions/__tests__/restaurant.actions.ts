import configureMockStore from "redux-mock-store";
import thunk from "redux-thunk";

import { 
  GET_RESTAURANTS_LOADING, 
  GET_RESTAURANTS_SUCCESS, 
  GET_RESTAURANTS_FAILURE
} from '../../types';
import { 
  RestaurantResponse 
} from '../../providers/RestaurantProvider';
import { getRestaurantsByCategory } from "../restaurant.actions";

import restaurantsResponseJson from "../../../../dependencies/stubby/mocks/response/restaurants_response.json";

const mockStore = configureMockStore([thunk]);

const mockFetchRestaurantsByCategory = jest.fn();
jest.mock('../../providers/RestaurantProvider', () => ({
  fetchRestaurantsByCategory: (category: string) => mockFetchRestaurantsByCategory(category)
}));

describe("restaurant.actions", () => {
  let store: any;

  beforeEach(() => {
    mockFetchRestaurantsByCategory.mockClear();

    store = mockStore({
      restaurant: {}
    });
  });

  it("should return a loading and success with payload RestaurantResponse", async () => {
    mockFetchRestaurantsByCategory.mockImplementationOnce(() =>
      Promise.resolve(
        restaurantsResponseJson.data as RestaurantResponse[]
      )
    );

    const category = "pizza";

    await store.dispatch(getRestaurantsByCategory(category));
    const actions = store.getActions();

    expect(mockFetchRestaurantsByCategory).toHaveBeenCalledTimes(1);

    expect(mockFetchRestaurantsByCategory.mock.calls[0][0]).toBe(category)

    expect(actions[0].type).toEqual(GET_RESTAURANTS_LOADING);
    expect(actions[0].loading).toEqual(true);
    expect(actions[0].error).toEqual(false);

    expect(actions[1].type).toEqual(GET_RESTAURANTS_SUCCESS);
    expect(actions[1].loading).toEqual(false);
    expect(actions[1].error).toEqual(false);
    expect(actions[1].payload.length).toEqual(3);

    expect(actions[1].payload[0].uuid).toEqual("cbb9c2bd-abde-48a3-891a-6229fc9b7c2f");
    expect(actions[1].payload[0].name).toEqual("Pizza Hut");
    expect(actions[1].payload[0].logo).toEqual("https://pbs.twimg.com/profile_images/1333417326704791553/Mm0bj3oN.jpg");
    expect(actions[1].payload[0].description).toEqual("Pizza Hut é uma cadeia de restaurantes e franquias especializada em pizzas e massas. Com sede na cidade de Plano, no Texas, a Pizza Hut é a maior cadeia de pizzarias do mundo, com quase 15 mil restaurantes e quiosques em mais de 130 países. Possui 95 restaurantes no Brasil e 91 em Portugal.");
    expect(actions[1].payload[0].address).toEqual("Av. Nome da avenida, 123");

    expect(actions[1].payload[1].uuid).toEqual("23e0211c-19ea-47ee-b98e-77e023e1a95f");
    expect(actions[1].payload[1].name).toEqual("Bráz Pizzaria");
    expect(actions[1].payload[1].logo).toEqual("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwwg9Nscz6dkUtIpPUB0u1tAxfZmJcmcfGTA&usqp=CAU");
    expect(actions[1].payload[1].description).toEqual("Pizzaria reúne ingredientes clássicos e sofisticados e muito burburinho e aconchego frente aos fornos à lenha.");
    expect(actions[1].payload[1].address).toEqual("Av. Nome da outra avenida, 789");

    expect(actions[1].payload[2].uuid).toEqual("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c");
    expect(actions[1].payload[2].name).toEqual("Domino's Pizza");
    expect(actions[1].payload[2].logo).toEqual("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQd7Ow6kgZoZk4h4oybgOkRJf6LZ_NLpBhfRA&usqp=CAU");
    expect(actions[1].payload[2].description).toEqual("Domino's Pizza é uma empresa de alimentação baseada em pizzas. Atualmente, é a maior rede de entregas de pizzas do mundo, com 13.000 lojas em 83 países");
    expect(actions[1].payload[2].address).toEqual("Rua Nome da rua, 654");
  });

  it("should return a loading and success with empty payload of RestaurantResponse", async () => {
    mockFetchRestaurantsByCategory.mockImplementationOnce(() =>
      Promise.resolve(
        []
      )
    );

    const category = "hamburguer";

    await store.dispatch(getRestaurantsByCategory(category));
    const actions = store.getActions();

    expect(mockFetchRestaurantsByCategory).toHaveBeenCalledTimes(1);

    expect(mockFetchRestaurantsByCategory.mock.calls[0][0]).toBe(category)

    expect(actions[0].type).toEqual(GET_RESTAURANTS_LOADING);
    expect(actions[0].loading).toEqual(true);
    expect(actions[0].error).toEqual(false);

    expect(actions[1].type).toEqual(GET_RESTAURANTS_SUCCESS);
    expect(actions[1].loading).toEqual(false);
    expect(actions[1].error).toEqual(false);
    expect(actions[1].payload.length).toEqual(0);
  });

  it("should return a loading and failure", async () => {
    mockFetchRestaurantsByCategory.mockImplementationOnce(() =>
      null
    );

    const category = "hamburguer";

    await store.dispatch(getRestaurantsByCategory(category));
    const actions = store.getActions();

    expect(mockFetchRestaurantsByCategory).toHaveBeenCalledTimes(1);

    expect(mockFetchRestaurantsByCategory.mock.calls[0][0]).toBe(category)

    expect(actions[0].type).toEqual(GET_RESTAURANTS_LOADING);
    expect(actions[0].loading).toEqual(true);
    expect(actions[0].error).toEqual(false);

    expect(actions[1].type).toEqual(GET_RESTAURANTS_FAILURE);
    expect(actions[1].loading).toEqual(false);
    expect(actions[1].error).toEqual(true);
    expect(actions[1].payload).toBeUndefined();
  });
});