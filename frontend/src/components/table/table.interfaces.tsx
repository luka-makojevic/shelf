import { FaTrash, FaEdit } from 'react-icons/fa';
import { RiArrowGoBackFill } from 'react-icons/ri';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { DeleteIconContainer, IconContainer } from './table.styles';

export interface RowProps {
  data: TableDataTypes;
  multiSelect?: boolean;
  selectedRows: TableDataTypes[];
  setSelectedRows: (data: TableDataTypes[]) => void;
  isChecked?: boolean;
  path: string;
  actions?: ActionType[];
}

export interface ActionType {
  comp: ({ onClick }: { onClick: () => void }) => JSX.Element;
  handler: (row: TableDataTypes) => void;
  key: number;
}

export const Delete = ({ onClick }: { onClick: () => void }) => (
  <DeleteIconContainer>
    <FaTrash onClick={onClick} />
  </DeleteIconContainer>
);

export const Edit = ({ onClick }: { onClick: () => void }) => (
  <IconContainer>
    <FaEdit onClick={onClick} />
  </IconContainer>
);

export const Recover = ({ onClick }: { onClick: () => void }) => (
  <IconContainer>
    <RiArrowGoBackFill onClick={onClick} />
  </IconContainer>
);
