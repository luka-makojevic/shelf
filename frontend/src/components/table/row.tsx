import { FaEdit, FaFile, FaFolder, FaTrash } from 'react-icons/fa';
import { RiArrowGoBackFill } from 'react-icons/ri';
import { useNavigate, useLocation } from 'react-router-dom';
import { TableDataTypes } from '../../interfaces/dataTypes';
import CheckBox from '../UI/checkbox/checkBox';
import {
  StyledRow,
  StyledCell,
  DeleteActionContainer,
  IconContainer,
} from './table.styles';

interface RowProps {
  data: TableDataTypes;
  multiSelect?: boolean;
  selectedRows: TableDataTypes[];
  setSelectedRows: (data: TableDataTypes[]) => void;
  isChecked?: boolean;
  path: string;
  onDelete?: (shelf: TableDataTypes) => void;
  onEdit?: (data: TableDataTypes) => void;
  onRecoverFromTrash?: (data: TableDataTypes) => void;
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
  onRecoverFromTrash,
}: RowProps) => {
  const navigation = useNavigate();
  const location = useLocation();

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

  const handleRecoverFromTrash = () => {
    if (onRecoverFromTrash) onRecoverFromTrash(data);
  };

  return (
    <StyledRow>
      {multiSelect && (
        <StyledCell>
          <CheckBox onChange={handleChange} checked={isChecked} />
        </StyledCell>
      )}

      {Object.values(data).map((rowText) => {
        if (rowText === data.id || rowText === data.folder) return null;
        if (
          rowText === data.id ||
          rowText === data.folder ||
          typeof rowText === 'boolean'
        )
          return null;

        return (
          <CellWithHandler
            key={`${data.id}-${rowText}`}
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

          <IconContainer>
            <FaEdit onClick={handleEdit} />
          </IconContainer>
        </StyledCell>
      )}
      {multiSelect && (
        <StyledCell>
          <IconContainer>
            {location.pathname.includes('/dashboard/trash') ? (
              <RiArrowGoBackFill onClick={handleRecoverFromTrash} />
            ) : (
              <FaEdit onClick={handleEdit} />
            )}
          </IconContainer>
        </StyledCell>
      )}
    </StyledRow>
  );
};
