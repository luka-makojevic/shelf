import styled from 'styled-components';
import { theme } from '../../theme';

export const Thead = styled.thead`
  background: ${theme.colors.secondary};
  padding: ${theme.space.sm} 0;
  text-align: left;

  position: sticky;
  top: 0;
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
export const TableHeaderInner = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const StyledTableContainer = styled.div`
  overflow: auto;
  max-height: 390px;
  position: relative;
`;

export const StyledTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  height: 100%;
  position: relative;

  @media (max-width: ${theme.breakpoints.sm}) {
    display: block;
    white-space: nowrap;
  }
`;

export const StyledCell = styled.td`
  border-bottom: 1px solid #d0d0d0;
  padding: ${theme.space.sm};
  border-collapse: collapse;
  cursor: pointer;
`;
export const StyledRow = styled.tr`
  &:last-child {
    ${StyledCell} {
      border: none;
    }
  }

  &:hover {
    background-color: ${theme.colors.lightBlue};
  }
`;

export const DeleteActionContainer = styled.span`
  margin-right: ${theme.space.sm};

  &:hover {
    color: ${theme.colors.danger};
  }
`;

export const IconContainer = styled.div`
  display: inline-block;
  margin-right: ${theme.space.sm};

  &:hover {
    color: ${theme.colors.primary};
  }
`;

export const DeleteIconContainer = styled.span`
  margin-right: ${theme.space.sm};

  &:hover {
    color: ${theme.colors.danger};
  }
`;
