/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, fireEvent, screen, cleanup } from '@testing-library/react';
import ReactDOM from 'react-dom';
import SearchBar from './searchBar';

const data = [
  {
    name: 'Sanja',
    id: 1,
    fullName: 'Sanja Nikolic',
  },
  {
    name: 'Vanja',
    id: 2,
    fullName: 'Vanja Nikolic',
  },
  {
    name: 'Janja',
    id: 3,
    fullName: 'Janja Nikolic',
  },
  {
    name: 'Sara',
    id: 111,
    fullName: 'Sara Popovic',
  },
];

const setData = jest.fn(() => {});

afterEach(cleanup);

test('SearchBar renders properly', () => {
  const div = document.createElement('div');
  ReactDOM.render(
    <SearchBar data={data} setData={setData} searchKey="name" />,
    div
  );
});

test('SearchBar placeholder renders properly', () => {
  render(
    <SearchBar
      setData={setData}
      data={data}
      searchKey="name"
      placeholder="Search..."
    />
  );

  const searchPlaceholder = screen.getByPlaceholderText(
    'Search...'
  ) as HTMLInputElement;

  expect(searchPlaceholder.placeholder).toBeTruthy();
});

test('SearchBar input on change works properly', () => {
  const { getByPlaceholderText } = render(
    <SearchBar
      data={data}
      setData={setData}
      searchKey="name"
      placeholder="Search..."
    />
  );

  const input = getByPlaceholderText('Search...') as HTMLInputElement;

  fireEvent.change(input, {
    target: { value: 'a' },
  });

  expect(input.value).toEqual('a');
});

test('SearchBar input should be empty', () => {
  const { getByPlaceholderText } = render(
    <SearchBar
      data={data}
      setData={setData}
      searchKey="name"
      placeholder="Search..."
    />
  );

  const input = getByPlaceholderText('Search...') as HTMLInputElement;

  expect(input.value).toEqual('');
});

test('Should return an array of objects that contain key name whose value contains "s"', () => {
  const searchInput = 's';
  const searchKey = 'name';

  const filteredData = data.filter((item: any) =>
    item[searchKey].toString().toLowerCase().includes(searchInput.toLowerCase())
  );

  expect(filteredData).toEqual(
    expect.arrayContaining([
      expect.objectContaining({ name: 'Sanja' }),
      expect.objectContaining({ name: 'Sara' }),
    ])
  );
});

test('Should return an array of objects that contain key fullName whose value contains "VA" regardles of case', () => {
  const searchInput = 'VA';
  const searchKey = 'fullName';

  const filteredData = data.filter((item: any) =>
    item[searchKey].toString().toLowerCase().includes(searchInput.toLowerCase())
  );

  expect(filteredData).toEqual(
    expect.arrayContaining([
      expect.objectContaining({ fullName: 'Vanja Nikolic' }),
    ])
  );
});

test('Should return an array of objects that contain key id whose value contains "1"', () => {
  const searchInput = '1';
  const searchKey = 'id';

  const filteredData = data.filter((item: any) =>
    item[searchKey].toString().toLowerCase().includes(searchInput.toLowerCase())
  );

  expect(filteredData).toEqual(
    expect.arrayContaining([
      expect.objectContaining({ id: 111 }),
      expect.objectContaining({ id: 1 }),
    ])
  );
});

test('Should return an array of objects that contain key id whose value contains "a Nikolic"', () => {
  const searchInput = 'a Nikolic';
  const searchKey = 'fullName';

  const filteredData = data.filter((item: any) =>
    item[searchKey].toString().toLowerCase().includes(searchInput.toLowerCase())
  );

  expect(filteredData).toEqual(
    expect.arrayContaining([
      expect.objectContaining({ fullName: 'Sanja Nikolic' }),
      expect.objectContaining({ fullName: 'Vanja Nikolic' }),
      expect.objectContaining({ fullName: 'Janja Nikolic' }),
    ])
  );
});

test('Should return an empty array', () => {
  const searchInput = 'dddd';
  const searchKey = 'fullName';

  const filteredData = data.filter((item: any) =>
    item[searchKey].toString().toLowerCase().includes(searchInput.toLowerCase())
  );

  expect(filteredData).toEqual([]);
});
