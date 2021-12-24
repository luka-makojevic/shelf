import { FaEdit, FaTrash, FaFolder, FaFile } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { theme } from '../../theme';
import CheckBox from '../UI/checkbox/checkBox';
import { StyledRow, StyledCell, IconContainer } from './table -styles';

interface RowProps {
  data: TableDataTypes;
  multiSelect?: boolean;
  selectedRows: TableDataTypes[];
  setSelectedRows: (data: TableDataTypes[]) => void;
  isChecked?: boolean;
  path: string;
  onDelete?: (shelf: TableDataTypes) => void;
  onEdit?: (data: TableDataTypes) => void;
}

export const DashboardTableRow = ({
  data,
  multiSelect,
  selectedRows,
  setSelectedRows,
  isChecked,
  path,
  onDelete,
  onEdit,
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
        selectedRows.filter((row: TableDataTypes) => row.id !== data.id)
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
      if (data.folder || data.folder === undefined) navigation(pathName);
    };

    return (
      <StyledCell onClick={handleClick}>
        {data.folder !== undefined && (
          <IconContainer>
            {rowText === data.name && (data.folder ? <FaFolder /> : <FaFile />)}
          </IconContainer>
        )}
        {rowText}
      </StyledCell>
    );
  };

  const handleDelete = () => {
    if (onDelete) onDelete(data);
  };

  const handleEdit = () => {
    if (onEdit) onEdit(data);
  };

  return (
    <StyledRow>
      {multiSelect && (
        <StyledCell>
          <CheckBox onChange={handleChange} checked={isChecked} />
        </StyledCell>
      )}
      {Object.values(data).map((rowText) => {
        if (
          rowText === data.id ||
          rowText === data.folder ||
          typeof rowText === 'boolean'
        )
          return null;

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
          <IconContainer>
            <FaTrash fill={theme.colors.danger} onClick={handleDelete} />
          </IconContainer>
          <FaEdit fill={theme.colors.black} onClick={handleEdit} />
        </StyledCell>
      )}
      {multiSelect && (
        <StyledCell>
          <FaEdit fill={theme.colors.black} onClick={handleEdit} />
        </StyledCell>
      )}
    </StyledRow>
  );
};
