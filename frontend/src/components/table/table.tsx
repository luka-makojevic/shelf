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
} from './table -styles';
import {
  FileTableDataTypes,
  FunctionTableDataTypes,
  HeaderTypes,
  ShelfTableDataTypes,
  SortingDirectionTypes,
  TableDataTypes,
} from '../../interfaces/dataTypes';
import { SortingDirection } from '../../utils/enums/table';

interface TableProps {
  mulitSelect?: boolean;
  data: TableDataTypes[];
  headers: HeaderTypes[];
  setTableData: Dispatch<SetStateAction<TableDataTypes[]>>;
  path: string;
}

export const Table = ({
  mulitSelect,
  data,
  headers,
  setTableData,
  path,
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
    } else {
      setSelectAll(true);
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
          {!mulitSelect && <StyledTableHeader>Actions</StyledTableHeader>}
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
            isChecked={selectedRows.some(
              (
                rowData:
                  | FunctionTableDataTypes
                  | FileTableDataTypes
                  | ShelfTableDataTypes
              ) => rowData.id === item.id
            )}
          />
        ))}
      </tbody>
    </StyledTable>
  );
};
