import {
  TableContainer,
  TableContent,
  TableDescription,
  TableTitle,
} from './tableWrapper-styles';

export interface TableWrapperProps {
  title?: string;
  description?: string;
  children: JSX.Element | JSX.Element[];
}

const TableWrapper = ({ title, description, children }: TableWrapperProps) => (
  <TableContainer>
    <TableTitle>{title}</TableTitle>
    <TableDescription>{description}</TableDescription>
    <TableContent>{children}</TableContent>
  </TableContainer>
);

export default TableWrapper;
