import {
  FaEdit,
  FaFile,
  FaFolder,
  FaTrash,
  FaTrashRestore,
} from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { TableDataTypes } from '../../interfaces/dataTypes';
import CheckBox from '../UI/checkbox/checkBox';
import {
  StyledRow,
  StyledCell,
  ActionContainer,
  DeleteActionContainer,
  IconContainer,
} from './table -styles';

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
      navigation(pathName);
    };

    return <StyledCell onClick={handleClick}>{rowText}</StyledCell>;
  };

  const handleDelete = () => {
    if (onDelete) onDelete(data);
  };

  const handleEdit = () => {
    if (onEdit) onEdit(data);
  };

  const handleRestore = () => {};

  return (
    <StyledRow>
      {multiSelect && (
        <StyledCell>
          <CheckBox onChange={handleChange} checked={isChecked} />
        </StyledCell>
      )}

      {Object.values(data).map((rowText) => {
        if (rowText === data.id || rowText === data.folder) return null;
        if (path === 'trash/') {
          return (
            // if it's in trash, there should be no navigation handled, only display of cells
            <StyledCell key={rowText}>
              <IconContainer>
                {data.name === rowText &&
                  (data.folder ? <FaFolder /> : <FaFile />)}
              </IconContainer>
              {rowText}
            </StyledCell>
          );
        }
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
          <DeleteActionContainer>
            <FaTrash onClick={handleDelete} />
          </DeleteActionContainer>

          <ActionContainer>
            {path === 'trash/' ? (
              <FaTrashRestore onClick={handleRestore} />
            ) : (
              <FaEdit onClick={handleEdit} />
            )}
          </ActionContainer>
        </StyledCell>
      )}
    </StyledRow>
  );
};
