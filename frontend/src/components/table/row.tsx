/* eslint-disable react/prop-types */
import { FaFile, FaFolder } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { Role } from '../../utils/enums/roles';
import CheckBox from '../UI/checkbox/checkBox';
import { ActionType, RowProps } from './table.interfaces';
import { StyledRow, StyledCell, IconContainer } from './table.styles';

export const Row = ({
  data,
  multiSelect,
  selectedRows,
  setSelectedRows,
  isChecked,
  path,
  actions,
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
    rowText: string | number | { id: Role; name: string };
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

  const ActionWithHandler = ({ action }: { action: ActionType }) => {
    const handleClick = () => {
      action.handler(data);
    };

    return <action.comp key={action.key} onClick={handleClick} />;
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
      <StyledCell>
        {actions &&
          actions.map((action: ActionType) => (
            <ActionWithHandler action={action} key={action.key} />
          ))}
      </StyledCell>
    </StyledRow>
  );
};
