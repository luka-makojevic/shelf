// import { useEffect, useState } from 'react';
import { FaEdit, FaTrash } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { TableDataTypes, UserType } from '../../interfaces/dataTypes';
// import userServices from '../../services/userServices';
import { theme } from '../../theme';
import CheckBox from '../UI/checkbox/checkBox';
import { StyledRow, StyledCell, ActionContainer } from './table -styles';

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

  // const [user, setUser] = useState<UserType>();

  // NE RADI JER JE OVO IZ ADMIN SERVIC-A I OBIVAN USER NEMA PERMISSION DA DOBIJE USER-A
  // useEffect(() => {
  //   console.log('ok');
  //   if (path === 'shared/') {
  //     userServices
  //       .getUserById(data.sharedBy)
  //       .then((res) => {
  //         setUser(res.data);
  //         console.log('ok');
  //       })
  //       .catch((err) => {
  //         console.log(err.response.data.message);
  //       });
  //   }
  // }, []);

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

  return (
    <StyledRow>
      {multiSelect && (
        <StyledCell>
          <CheckBox onChange={handleChange} checked={isChecked} />
        </StyledCell>
      )}
      {Object.values(data).map((rowText) => {
        if (rowText === data.id) return null;
        // if (rowText === user?.id) {
        //   console.log(user.email);
        //   return <StyledCell>{user.email}</StyledCell>;
        // }
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
            <FaTrash fill={theme.colors.danger} onClick={handleDelete} />
          </ActionContainer>
          <FaEdit fill={theme.colors.black} onClick={handleEdit} />
        </StyledCell>
      )}
    </StyledRow>
  );
};
