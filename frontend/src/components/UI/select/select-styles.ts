import styled from 'styled-components';
import { theme } from '../../../theme';

export interface SelectStyleProps {
  variant?: 'secondary' | undefined;
}

export const DropDownContainer = styled.div`
  width: 100%;
  margin: 0;
  color: black;
  margin: ${theme.space.sm} 0 ${theme.space.lg};
  input {
    display: none;
  }
  height: 50px;
  font-size: ${theme.fontSizes.sm};
`;

export const DropDownHeader = styled.div<SelectStyleProps>`
  background: ${theme.colors.white};
  color: ${theme.colors.black};

  padding: ${theme.space.sm};
  height: 45px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: ${theme.space.lg};
  margin-bottom: 10px;
  text-indent: 10px;
  cursor: pointer;

  ${({ variant }) => {
    switch (variant) {
      case 'secondary':
        return `
          border: 1px solid ${theme.colors.secondary};
        `;
      default:
        return null;
    }
  }}
`;

export const DropdownListContainer = styled.div`
  position: relative;
`;

export const DropDownList = styled.div<SelectStyleProps>`
  margin: 0;
  background: ${theme.colors.white};
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0px 0px 6px 2px rgba(0, 0, 0, 0.1);
  max-height: 180px;
  overflow: scroll;
  &::-webkit-scrollbar {
    display: none;
  }
`;

export const DropdownItem = styled.div`
  padding: 10px;
  &:hover {
    background: ${theme.colors.lightBlue};
  }
`;
