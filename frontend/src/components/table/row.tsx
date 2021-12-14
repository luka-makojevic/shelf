import { FaEdit, FaTrash } from 'react-icons/fa';
import {
  FileDataTypes,
  FunctionDataTypes,
  ShelfDataTypes,
} from '../../interfaces/dataTypes';
import CheckBox from '../UI/checkbox/checkBox';
import { StyledRow, StyledCell, ActionContainer } from './table -styles';

interface RowProps {
  data: FunctionDataTypes | FileDataTypes | ShelfDataTypes;
  multiSelect?: boolean;
  selectedRows: (FunctionDataTypes | FileDataTypes | ShelfDataTypes)[];
  setSelectedRows: (
    data: (FunctionDataTypes | FileDataTypes | ShelfDataTypes)[]
  ) => void;
  isChecked?: boolean;
}

export const Row = ({
  data,
  multiSelect,
  selectedRows,
  setSelectedRows,
  isChecked,
}: RowProps) => {
  const handleOnChange = () => {
    const alreadySelected = selectedRows.some(
      (row: FunctionDataTypes | FileDataTypes | ShelfDataTypes) =>
        row.id === data.id
    );

    if (!alreadySelected) {
      setSelectedRows([...selectedRows, data]);
    } else {
      setSelectedRows(
        selectedRows.filter(
          (row: FunctionDataTypes | FileDataTypes | ShelfDataTypes) =>
            row.id !== data.id
        )
      );
    }
  };

  return (
    <StyledRow>
      {multiSelect && (
        <StyledCell>
          <CheckBox onChange={handleOnChange} checked={isChecked} />
        </StyledCell>
      )}
      {Object.values(data).map((rowText, idx) => (
        <StyledCell key={`${rowText}-${idx}`}>{rowText}</StyledCell>
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
