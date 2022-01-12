import { useEffect, useState } from 'react';
import { FaCaretDown, FaCaretUp } from 'react-icons/fa';
import CheckBox from '../UI/checkbox/checkBox';
import { createSortingDirectons, sortColumn } from './sorter';
import {
  StyledTable,
  StyledTableHeader,
  Thead,
  CheckBoxTableHeader,
  TableHeaderInner,
  StyledTableContainer,
} from './table.styles';
import {
  HeaderTypes,
  SortingDirectionTypes,
  TableDataTypes,
} from '../../interfaces/dataTypes';
import { SortingDirection } from '../../utils/enums/table';
import { ActionType } from './table.interfaces';
import { Row } from './row';

interface TableProps {
  mulitSelect?: boolean;
  data: TableDataTypes[];
  headers: HeaderTypes[];
  setTableData: (data: TableDataTypes[]) => void;
  path: string;
  getSelectedRows?: (data: TableDataTypes[]) => void;
  actions?: ActionType[];
}

export const Table = ({
  mulitSelect,
  data,
  headers,
  setTableData,
  path,
  getSelectedRows,
  actions,
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
            <Row
              actions={actions}
              key={item.name}
              selectedRows={selectedRows}
              setSelectedRows={setSelectedRows}
              multiSelect={mulitSelect}
              data={item}
              path={path}
              isChecked={selectedRows.some(
                (rowData: TableDataTypes) =>
                  rowData.id === item.id && rowData.folder === item.folder
              )}
            />
          ))}
        </tbody>
      </StyledTable>
    </StyledTableContainer>
  );
};
