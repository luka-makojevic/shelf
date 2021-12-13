/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useState, useEffect, Dispatch, SetStateAction } from 'react';
import { FaSearch } from 'react-icons/fa';
import { theme } from '../../theme';
import {
  SearchIconContainer,
  SearchInputContainer,
  SearchInputField,
} from './searchBar-styles';

interface SearchProps {
  setData: Dispatch<SetStateAction<any>>;
  data: any;
  searchKey: string;
  placeholder?: string;
}

const SearchBar = ({ setData, data, searchKey, ...restProps }: SearchProps) => {
  const [searchInput, setSearchInput] = useState('');

  const handleSearchchange = (e: React.FormEvent<HTMLInputElement>) => {
    setSearchInput(e.currentTarget.value);
  };

  useEffect(() => {
    if (!searchInput) {
      setData(data);
    } else {
      setData(
        data.filter((item: any) =>
          item[searchKey]
            .toString()
            .toLowerCase()
            .split(' ')
            .join('')
            .includes(searchInput.toLowerCase().trim().split(' ').join(''))
        )
      );
    }
  }, [searchInput]);

  return (
    <SearchInputContainer>
      <SearchInputField
        id="searchBar"
        type="text"
        {...restProps}
        onChange={handleSearchchange}
      />
      <SearchIconContainer>
        <FaSearch color={theme.colors.secondary} />
      </SearchIconContainer>
    </SearchInputContainer>
  );
};

export default SearchBar;
