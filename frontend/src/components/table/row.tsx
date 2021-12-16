import { FaEdit, FaTrash } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import {
  FileTableDataTypes,
  FunctionTableDataTypes,
  ShelfTableDataTypes,
  TableDataTypes,
} from '../../interfaces/dataTypes';
import { theme } from '../../theme';
import CheckBox from '../UI/checkbox/checkBox';
import { StyledRow, StyledCell, ActionContainer } from './table -styles';

interface RowProps {
  data: TableDataTypes;
  multiSelect?: boolean;
  selectedRows: (
    | FunctionTableDataTypes
    | FileTableDataTypes
    | ShelfTableDataTypes
  )[];
  setSelectedRows: (data: TableDataTypes[]) => void;
  isChecked?: boolean;
  path: string;
}

export const DashboardTableRow = ({
  data,
  multiSelect,
  selectedRows,
  setSelectedRows,
  isChecked,
  path,
}: RowProps) => {
  const navigation = useNavigate();

  const handleChange = () => {
    const alreadySelected = selectedRows.some(
      (row: TableDataTypes) => row.id === data.id
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

  const CellWithHandler = ({
    rowText,
    pathName,
  }: {
    rowText: string | number;
    pathName: string;
  }) => {
    const handleClick = () => {
      navigation(pathName);
    };

    return <StyledCell onClick={handleClick}>{rowText}</StyledCell>;
  };

  return (
    <StyledRow>
      {multiSelect && (
        <StyledCell>
          <CheckBox onChange={handleChange} checked={isChecked} />
        </StyledCell>
      )}
      {Object.values(data).map((rowText) => {
        if (rowText === data.id) return null;
        return (
          <CellWithHandler
            key={rowText}
            rowText={rowText}
            pathName={`${path}${data.id}`}
          />
        );
      })}
      {!multiSelect && (
        <StyledCell>
          <ActionContainer>
            <FaTrash fill={theme.colors.danger} />
          </ActionContainer>
          <FaEdit fill={theme.colors.black} />
        </StyledCell>
      )}
    </StyledRow>
  );
};
