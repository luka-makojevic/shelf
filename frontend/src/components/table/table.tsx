import { Dispatch, SetStateAction, useEffect, useState } from 'react';
import { FaCaretDown, FaCaretUp } from 'react-icons/fa';
import CheckBox from '../UI/checkbox/checkBox';
import { DashboardTableRow } from './row';
import { createSortingDirectons, sortColumn } from './sorter';
import {
  StyledTable,
  StyledTableHeader,
  Thead,
  CheckBoxTableHeader,
  TableHeaderInner,
  StyledTableContainer,
} from './table -styles';
import {
  HeaderTypes,
  SortingDirectionTypes,
  TableDataTypes,
} from '../../interfaces/dataTypes';
import { SortingDirection } from '../../utils/enums/table';

interface TableProps {
  mulitSelect?: boolean;
  data: TableDataTypes[];
  headers: HeaderTypes[];
  setTableData: (data: TableDataTypes[]) => void;
  path: string;
  onDelete?: (shelf: TableDataTypes) => void;
  onEdit?: (data: TableDataTypes) => void;
  getSelectedRows?: (data: TableDataTypes[]) => void;
}

export const Table = ({
  mulitSelect,
  data,
  headers,
  setTableData,
  path,
  onDelete,
  onEdit,
  getSelectedRows,
}: TableProps) => {
  const [selectAll, setSelectAll] = useState<boolean>(false);
  const [selectedRows, setSelectedRows] = useState<TableDataTypes[]>([]);
  const [sortingDirections, setSortingDirections] =
    useState<SortingDirectionTypes>({});

  const handleSelectAll = () => {
    if (selectedRows.length !== data.length) {
      setSelectedRows(data);
      setSelectAll(true);
    } else {
      setSelectedRows([]);
      setSelectAll(false);
    }
  };

  useEffect(() => {
    if (selectedRows.length !== data.length) {
      setSelectAll(false);
    } else if (data.length !== 0) {
      setSelectAll(true);
    }
    if (getSelectedRows) {
      getSelectedRows(selectedRows);
    }
  }, [selectedRows]);

  useEffect(() => {
    createSortingDirectons(headers, setSortingDirections);
  }, []);

  const sorterArrowToggle = (key: string) => {
    if (
      sortingDirections[key] === SortingDirection.UNSORTED ||
      sortingDirections[key] === SortingDirection.ASCENDING
    )
      return <FaCaretDown />;
    return <FaCaretUp />;
  };
  const handleClick = (key: string) => {
    sortColumn(
      key,
      sortingDirections,
      data,
      setTableData,
      setSortingDirections
    );
  };

  return (
    <StyledTableContainer>
      <StyledTable>
        <Thead>
          <tr>
            {mulitSelect && (
              <CheckBoxTableHeader>
                <CheckBox onChange={handleSelectAll} checked={selectAll} />
              </CheckBoxTableHeader>
            )}
            {headers.map(({ header, key }) => (
              <StyledTableHeader onClick={() => handleClick(key)} key={header}>
                <TableHeaderInner>
                  {header}
                  {sorterArrowToggle(key)}
                </TableHeaderInner>
              </StyledTableHeader>
            ))}
            <StyledTableHeader>Actions</StyledTableHeader>
          </tr>
        </Thead>
        <tbody>
          {data.map((item) => (
            <DashboardTableRow
              key={item.name}
              selectedRows={selectedRows}
              setSelectedRows={setSelectedRows}
              multiSelect={mulitSelect}
              data={item}
              path={path}
              onDelete={onDelete}
              onEdit={onEdit}
              isChecked={selectedRows.some(
                (rowData: TableDataTypes) => rowData.id === item.id
              )}
            />
          ))}
        </tbody>
      </StyledTable>
    </StyledTableContainer>
  );
};
