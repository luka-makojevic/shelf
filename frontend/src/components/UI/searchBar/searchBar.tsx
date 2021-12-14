/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { Dispatch, SetStateAction } from 'react';
import { FaSearch } from 'react-icons/fa';
import { theme } from '../../../theme';
import {
  SearchIconContainer,
  SearchInputContainer,
  SearchInputField,
} from './searchBar-styles';

interface SearchProps {
  setData?: Dispatch<SetStateAction<any>>;
  data?: any;
  searchKey?: string;
  placeholder?: string;
  onChange?: React.ChangeEventHandler<HTMLInputElement>;
}

const SearchBar = ({
  setData,
  data,
  searchKey,
  onChange,
  ...restProps
}: SearchProps) => {
  const handleSearchchange = (e: React.FormEvent<HTMLInputElement>) => {
    if (setData !== undefined && searchKey !== undefined) {
      if (!e.currentTarget.value) {
        setData(data);
      } else {
        setData(
          data.filter((item: any) =>
            item[searchKey]
              .toString()
              .toLowerCase()
              .includes(e.currentTarget.value.toLowerCase().trim())
          )
        );
      }
    }
  };

  return (
    <SearchInputContainer>
      <SearchInputField
        id="searchBar"
        type="text"
        {...restProps}
        onChange={data && searchKey ? handleSearchchange : onChange}
      />
      <SearchIconContainer>
        <FaSearch color={theme.colors.secondary} />
      </SearchIconContainer>
    </SearchInputContainer>
  );
};

export default SearchBar;
