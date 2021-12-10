/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useState, useEffect, Dispatch, SetStateAction } from 'react';
import { FaSearch } from 'react-icons/fa';
import { theme } from '../../theme';
import {
  SearchIconContainer,
  SearchInputContainer,
  SearchInputField,
} from './searchBar-styles';

export interface SearchProps {
  setData: Dispatch<SetStateAction<any>>;
  data: any;
  searchKey: string;
}

const SearchBar = ({ setData, data, searchKey }: SearchProps) => {
  const [searchTerm, setSearchTerm] = useState('');

  const handleSearchchange = (e: React.FormEvent<HTMLInputElement>) => {
    setSearchTerm(e.currentTarget.value);
  };

  useEffect(() => {
    if (!searchTerm) {
      setData(data);
    } else {
      setData(
        data.filter((item: any) =>
          item[searchKey].toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
    }
  }, [searchTerm]);

  return (
    <SearchInputContainer>
      <SearchInputField
        type="text"
        placeholder="Search... "
        onChange={handleSearchchange}
      />
      <SearchIconContainer>
        <FaSearch color={theme.colors.secondary} />
      </SearchIconContainer>
    </SearchInputContainer>
  );
};

export default SearchBar;
