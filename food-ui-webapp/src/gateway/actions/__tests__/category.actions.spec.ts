import configureMockStore from "redux-mock-store";
import thunk from "redux-thunk";

import { 
  GET_CATEGORIES_LOADING, 
  GET_CATEGORIES_SUCCESS, 
  GET_CATEGORIES_FAILURE
} from '../../types';
import { 
  CategoryResponse 
} from '../../providers/RestaurantProvider';
import { getCategories } from "../category.actions";

import categoriesResponseJson from "../../../../dependencies/stubby/mocks/response/categories_response.json";

const mockStore = configureMockStore([thunk]);

const mockGetAllCategories = jest.fn();
jest.mock('../../providers/RestaurantProvider', () => ({
  getAllCategories: () => mockGetAllCategories()
}));

describe("category.actions", () => {
  let store: any;

  beforeEach(() => {
    mockGetAllCategories.mockClear();

    store = mockStore({
      category: {}
    });
  });

  it("should return a loading and success with payload CategoryResponse", async () => {
    mockGetAllCategories.mockImplementationOnce(() =>
      Promise.resolve(
        categoriesResponseJson.data as CategoryResponse[]
      )
    );

    await store.dispatch(getCategories());
    const actions = store.getActions();

    expect(mockGetAllCategories).toHaveBeenCalledTimes(1);

    expect(actions[0].type).toEqual(GET_CATEGORIES_LOADING);
    expect(actions[0].loading).toEqual(true);
    expect(actions[0].error).toEqual(false);

    expect(actions[1].type).toEqual(GET_CATEGORIES_SUCCESS);
    expect(actions[1].loading).toEqual(false);
    expect(actions[1].error).toEqual(false);
    expect(actions[1].payload.length).toEqual(5);

    expect(actions[1].payload[0].uuid).toEqual("0eda194c-827c-4254-ada8-214115310fc6");
    expect(actions[1].payload[0].code).toEqual("pizza");
    expect(actions[1].payload[0].description).toEqual("Pizzaria");

    expect(actions[1].payload[1].uuid).toEqual("cc0cd1b9-4217-498d-8da8-661de03b86e5");
    expect(actions[1].payload[1].code).toEqual("hamburguer");
    expect(actions[1].payload[1].description).toEqual("Hambúrguer");

    expect(actions[1].payload[2].uuid).toEqual("577a9962-e9b7-46a9-abc8-211e42f6f1aa");
    expect(actions[1].payload[2].code).toEqual("japonesa");
    expect(actions[1].payload[2].description).toEqual("Japonesa");

    expect(actions[1].payload[3].uuid).toEqual("67e92973-f1ff-4cc3-b2c0-6485939ae442");
    expect(actions[1].payload[3].code).toEqual("vegetariana");
    expect(actions[1].payload[3].description).toEqual("Vegetariana");

    expect(actions[1].payload[4].uuid).toEqual("2358fb2d-f0dc-4a00-8b55-6fbbce158e91");
    expect(actions[1].payload[4].code).toEqual("brasileira");
    expect(actions[1].payload[4].description).toEqual("Brasileira");
  });

  it("should return a loading and success with empty payload of CategoryResponse", async () => {
    mockGetAllCategories.mockImplementationOnce(() =>
      Promise.resolve(
        []
      )
    );

    await store.dispatch(getCategories());
    const actions = store.getActions();

    expect(mockGetAllCategories).toHaveBeenCalledTimes(1);

    expect(actions[0].type).toEqual(GET_CATEGORIES_LOADING);
    expect(actions[0].loading).toEqual(true);
    expect(actions[0].error).toEqual(false);

    expect(actions[1].type).toEqual(GET_CATEGORIES_SUCCESS);
    expect(actions[1].loading).toEqual(false);
    expect(actions[1].error).toEqual(false);
    expect(actions[1].payload.length).toEqual(0);
  });

  it("should return a loading and failure", async () => {
    mockGetAllCategories.mockImplementationOnce(() =>
      null
    );

    await store.dispatch(getCategories());
    const actions = store.getActions();

    expect(mockGetAllCategories).toHaveBeenCalledTimes(1);

    expect(actions[0].type).toEqual(GET_CATEGORIES_LOADING);
    expect(actions[0].loading).toEqual(true);
    expect(actions[0].error).toEqual(false);

    expect(actions[1].type).toEqual(GET_CATEGORIES_FAILURE);
    expect(actions[1].loading).toEqual(false);
    expect(actions[1].error).toEqual(true);
    expect(actions[1].payload).toBeUndefined();
  });
});