import styled from 'styled-components';
import { theme } from '../../../theme';

export const StyledSelect = styled.select`
  width: 100%;
  border: none;
  border-radius: 10px;
  min-height: 45px;
  border: 1px solid ${theme.colors.secondary};
  border-radius: 30px;
  text-indent: 10px;
  margin: ${theme.space.xs} 0;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;

  &:focus {
    outline: none;
    box-shadow: 0 0 6px 0 ${theme.colors.secondary};
  }
  padding-left: 10px;
`;
export const SelectContainer = styled.div`
  position: relative;
  width: 100%;
  min-height: 75px;
  svg {
    position: absolute;
    right: 15px;
    top: 17px;
  }
`;
