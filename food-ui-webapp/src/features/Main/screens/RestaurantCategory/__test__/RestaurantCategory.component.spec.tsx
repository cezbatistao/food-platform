import { mocked } from 'ts-jest/utils';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import {Router} from 'react-router-dom';
import {createMemoryHistory} from 'history';

import { fetchCategories } from '../../../../../services/Category.services';
import RestaurantCategory from '../RestaurantCategory.component';
import Category from '../../../../../services/Category.model';
import CategoryContext from '../../../contexts/RestaurantCategory/RestaurantCategory.context';
import categoriesResponseJson from "../../../../../../dependencies/stubby/mocks/response/categories_response.json";


jest.mock('../../../../../services/Category.services');

let mockedFetchCategories: any

beforeEach(() => {
  jest.clearAllMocks()
  mockedFetchCategories = mocked(fetchCategories)
});

it('should list category empty list on component', async () => {

  mockedFetchCategories.mockImplementationOnce(() => Promise.resolve(
    []
  ));

  const history = createMemoryHistory();

  render(
    <Router history={history}>
      <RestaurantCategory />
    </Router>
  );

  const categoriesSelect = await waitFor(() => screen.getByTestId('select-category')) as HTMLSelectElement;

  expect(categoriesSelect.childElementCount).toEqual(1);

  const defaultOption = categoriesSelect.children[0] as HTMLOptionElement;
  expect(defaultOption.text).toEqual('Selecione uma categoria');
  expect(defaultOption.value).toEqual('');
  expect(defaultOption.selected).toBeTruthy();
});

it('should list category list on component', async () => {

  mockedFetchCategories.mockImplementationOnce(() => Promise.resolve(
    categoriesResponseJson.data as Category []
  ));

  const history = createMemoryHistory();

  render(
    <Router history={history}>
      <RestaurantCategory />
    </Router>
  );

  const categoriesSelect = await waitFor(() => screen.getByTestId('select-category')) as HTMLSelectElement;

  expect(categoriesSelect.childElementCount).toEqual(6);

  const defaultOption = categoriesSelect.children[0] as HTMLOptionElement;
  expect(defaultOption.text).toEqual('Selecione uma categoria');
  expect(defaultOption.value).toEqual('');
  expect(defaultOption.selected).toBeTruthy();

  const pizzeriaOption = categoriesSelect.children[1] as HTMLOptionElement;
  expect(pizzeriaOption.text).toEqual('Pizzaria');
  expect(pizzeriaOption.value).toEqual('pizza');
  expect(pizzeriaOption.selected).toBeFalsy();

  const hamburguerOption = categoriesSelect.children[2] as HTMLOptionElement;
  expect(hamburguerOption.text).toEqual('Hambúrguer');
  expect(hamburguerOption.value).toEqual('hamburguer');
  expect(hamburguerOption.selected).toBeFalsy();

  const japaneseOption = categoriesSelect.children[3] as HTMLOptionElement;
  expect(japaneseOption.text).toEqual('Japonesa');
  expect(japaneseOption.value).toEqual('japonesa');
  expect(japaneseOption.selected).toBeFalsy();

  const vegetarianOption = categoriesSelect.children[4] as HTMLOptionElement;
  expect(vegetarianOption.text).toEqual('Vegetariana');
  expect(vegetarianOption.value).toEqual('vegetariana');
  expect(vegetarianOption.selected).toBeFalsy();

  const brazilianOption = categoriesSelect.children[5] as HTMLOptionElement;
  expect(brazilianOption.text).toEqual('Brasileira');
  expect(brazilianOption.value).toEqual('brasileira');
  expect(brazilianOption.selected).toBeFalsy();
});

it('should select category and change state selected', async () => {

  mockedFetchCategories.mockImplementationOnce(() => Promise.resolve(
    categoriesResponseJson.data as Category []
  ));

  const categoryState = {
    category: '',
    setCategory: jest.fn(),
  };

  const history = createMemoryHistory();

  render(
    <CategoryContext.Provider value={ categoryState }>
      <Router history={history}>
        <RestaurantCategory />
      </Router>
    </CategoryContext.Provider>
  );

  await waitFor(() => {
    fireEvent.change(screen.getByTestId('select-category'), { 
      target: { value: 'pizza' } 
    });

    expect(categoryState.setCategory).toHaveBeenCalledWith('pizza');
  });

  const categoriesSelect = screen.getByTestId('select-category') as HTMLSelectElement;

  expect(categoriesSelect.childElementCount).toEqual(6);

  const defaultOption = categoriesSelect.children[0] as HTMLOptionElement;
  expect(defaultOption.text).toEqual('Selecione uma categoria');
  expect(defaultOption.value).toEqual('');
  expect(defaultOption.selected).toBeFalsy();

  const pizzeriaOption = categoriesSelect.children[1] as HTMLOptionElement;
  expect(pizzeriaOption.text).toEqual('Pizzaria');
  expect(pizzeriaOption.value).toEqual('pizza');
  expect(pizzeriaOption.selected).toBeTruthy();

  const hamburguerOption = categoriesSelect.children[2] as HTMLOptionElement;
  expect(hamburguerOption.text).toEqual('Hambúrguer');
  expect(hamburguerOption.value).toEqual('hamburguer');
  expect(hamburguerOption.selected).toBeFalsy();

  const japaneseOption = categoriesSelect.children[3] as HTMLOptionElement;
  expect(japaneseOption.text).toEqual('Japonesa');
  expect(japaneseOption.value).toEqual('japonesa');
  expect(japaneseOption.selected).toBeFalsy();

  const vegetarianOption = categoriesSelect.children[4] as HTMLOptionElement;
  expect(vegetarianOption.text).toEqual('Vegetariana');
  expect(vegetarianOption.value).toEqual('vegetariana');
  expect(vegetarianOption.selected).toBeFalsy();

  const brazilianOption = categoriesSelect.children[5] as HTMLOptionElement;
  expect(brazilianOption.text).toEqual('Brasileira');
  expect(brazilianOption.value).toEqual('brasileira');
  expect(brazilianOption.selected).toBeFalsy();
});