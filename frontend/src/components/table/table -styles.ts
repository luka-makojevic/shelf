import styled from 'styled-components';
import { theme } from '../../theme';

export const Thead = styled.thead`
  background: ${theme.colors.secondary};
  padding: ${theme.space.sm} 0;
  text-align: left;
`;
export const StyledTableHeader = styled.th`
  padding: ${theme.space.sm};
  user-select: none;
  cursor: pointer;
  position: relative;

  &:after {
    content: '';
    position: absolute;
    background: white;
    height: 70%;
    width: 1px;
    right: 0;
    top: 7px;
  }
  &:last-child {
    cursor: default;
    &:after {
      display: none;
    }
  }
`;

export const CheckBoxTableHeader = styled.th`
  padding: ${theme.space.sm};
  width: 0;
`;
export const TableHeaderInner = styled.th`
  display: flex;
  justify-content: space-between;
`;

export const StyledTable = styled.table`
  width: 100%;
  border-collapse: collapse;
`;
export const StyledCell = styled.td`
  border-bottom: 1px solid #d0d0d0;
  padding: ${theme.space.sm};
  border-collapse: collapse;
`;
export const StyledRow = styled.tr`
  &:last-child {
    ${StyledCell} {
      border: none;
    }
  }
`;

export const ActionContainer = styled.span`
  margin-right: ${theme.space.sm};
`;
