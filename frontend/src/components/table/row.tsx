import { FaEdit, FaTrash } from 'react-icons/fa';
import {
  FileTableDataTypes,
  FunctionTableDataTypes,
  ShelfTableDataTypes,
} from '../../interfaces/dataTypes';
import CheckBox from '../UI/checkbox/checkBox';
import { StyledRow, StyledCell, ActionContainer } from './table -styles';

interface RowProps {
  data: FunctionTableDataTypes | FileTableDataTypes | ShelfTableDataTypes;
  multiSelect?: boolean;
  selectedRows: (
    | FunctionTableDataTypes
    | FileTableDataTypes
    | ShelfTableDataTypes
  )[];
  setSelectedRows: (
    data: (FunctionTableDataTypes | FileTableDataTypes | ShelfTableDataTypes)[]
  ) => void;
  isChecked?: boolean;
}

export const DashboardTableRow = ({
  data,
  multiSelect,
  selectedRows,
  setSelectedRows,
  isChecked,
}: RowProps) => {
  
  const handleChange = () => {
    const alreadySelected = selectedRows.some(
      (
        row: FunctionTableDataTypes | FileTableDataTypes | ShelfTableDataTypes
      ) => row.id === data.id
    );

    if (!alreadySelected) {
      setSelectedRows([...selectedRows, data]);
    } else {
      setSelectedRows(
        selectedRows.filter(
          (
            row:
              | FunctionTableDataTypes
              | FileTableDataTypes
              | ShelfTableDataTypes
          ) => row.id !== data.id
        )
      );
    }
  };

  return (
    <StyledRow>
      {multiSelect && (
        <StyledCell>
          <CheckBox onChange={handleChange} checked={isChecked} />
        </StyledCell>
      )}
      {Object.values(data).map((rowText) => (
        <StyledCell key={rowText}>{rowText}</StyledCell>
      ))}
      {!multiSelect && (
        <StyledCell>
          <ActionContainer>
            <FaTrash />
          </ActionContainer>
          <FaEdit />
        </StyledCell>
      )}
    </StyledRow>
  );
};
