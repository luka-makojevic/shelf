import { useEffect, useState } from 'react';
import { FaCaretDown, FaCaretUp } from 'react-icons/fa';
import CheckBox from '../UI/checkbox/checkBox';
import { DashboardTableRow } from './row';
import { createSortingDirectons, sortColumn } from './sorter';
import {
  ArrowContainer,
  StyledTable,
  StyledTableHeader,
  Thead,
  CheckBoxTableHeader,
} from './table -styles';
import {
  FileDataTypes,
  FunctionDataTypes,
  HeaderTypes,
  ShelfDataTypes,
  SortingDirectionTypes,
} from '../../interfaces/dataTypes';
import { SortingDirection } from '../../utils/enums/table';

// for testing puropses
// const data: (FunctionDataTypes | FileDataTypes | ShelfDataTypes)[] = [
//   {
//     name: 'picture shelf',
//     creation_date: '11/8/1991',
//     id: 2,
//   },
//   { name: 'documents', creation_date: '11/1/2001', id: 1 },
//   { name: 'videos', creation_date: '11/4/2021', id: 3 },
// ];
// const headers: HeaderTypes[] = [
//   { header: 'Name', key: 'name' },
//   { header: 'Creation date', key: 'creation_date' },
//   { header: 'Id', key: 'id' },
// ];

interface TableProps {
  mulitSelect?: boolean;
  data: (FunctionDataTypes | FileDataTypes | ShelfDataTypes)[];
  headers: HeaderTypes[];
}

export const Table = ({ mulitSelect, data, headers }: TableProps) => {
  const [selectAll, setSelectAll] = useState<boolean>(false);
  const [selectedRows, setSelectedRows] = useState<
    (FunctionDataTypes | FileDataTypes | ShelfDataTypes)[]
  >([]);

  const [sortingDirections, setSortingDirections] =
    useState<SortingDirectionTypes>({});

  const [tableData, setTableData] =
    useState<(FunctionDataTypes | FileDataTypes | ShelfDataTypes)[]>(data);

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
  const handleOnClick = (key: string) => {
    sortColumn(
      key,
      sortingDirections,
      tableData,
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
            <StyledTableHeader
              onClick={() => handleOnClick(key)}
              key={`${header}`}
            >
              {header}
              <ArrowContainer>{sorterArrowToggle(key)}</ArrowContainer>
            </StyledTableHeader>
          ))}
          {!mulitSelect && <StyledTableHeader>Actions</StyledTableHeader>}
        </tr>
      </Thead>
      <tbody>
        {tableData.map((item) => (
          <DashboardTableRow
            key={`${item.name}`}
            selectedRows={selectedRows}
            setSelectedRows={setSelectedRows}
            multiSelect={mulitSelect}
            data={item}
            isChecked={selectedRows.some(
              (rowData: FunctionDataTypes | FileDataTypes | ShelfDataTypes) =>
                rowData.id === item.id
            )}
          />
        ))}
      </tbody>
    </StyledTable>
  );
};
