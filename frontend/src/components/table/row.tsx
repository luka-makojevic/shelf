import { FaEdit, FaTrash } from 'react-icons/fa';
import CheckBox from '../UI/checkbox/checkBox';
import { StyledRow, StyledCell, ActionContainer } from './table -styles';

interface RowProps {
  data: {
    name: string;
    creationDate: string;
    id: number;
  };
  multiSelect?: boolean;
  selectedRows: any;
  setSelectedRows: (
    data: {
      name: string;
      creationDate: string;
      id: number;
    }[]
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
    const alreadySelected = selectedRows.some((row: any) => row.id === data.id);

    if (!alreadySelected) {
      setSelectedRows([...selectedRows, data]);
    } else {
      setSelectedRows(selectedRows.filter((row: any) => row.id !== data.id));
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
