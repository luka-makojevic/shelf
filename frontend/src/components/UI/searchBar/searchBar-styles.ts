import styled from 'styled-components';
import { theme } from '../../../theme';

export const SearchInputField = styled.input`
  height: ${theme.space.lg};
  width: 100%;
  border-radius: 50px;
  text-indent: ${theme.space.lg};

  outline: none;

  border: 1px solid ${theme.colors.secondary};
`;

export const SearchInputContainer = styled.div`
  position: relative;
  width: 100%;
  max-width: 500px;
`;

export const SearchIconContainer = styled.div`
  position: absolute;
  width: 20px;
  height: 20px;
  left: ${theme.space.sm};
  top: ${theme.space.sm};
`;
