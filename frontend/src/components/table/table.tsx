import { useEffect, useState } from 'react';
import { FaCaretDown, FaCaretUp } from 'react-icons/fa';
import CheckBox from '../UI/checkbox/checkBox';
import { Row } from './row';
import { createSortingDirectons, sortColumn } from './sorter';
import {
  ArrowContainer,
  StyledTable,
  StyledTableheader,
  Thead,
} from './table -styles';
import { dataTypes, headerTypes } from '../../interfaces/dataTypes';

enum SortingDirection {
  ASCENDING = 'ASCENDING',
  DESCENDING = 'DESCENDING',
  UNSORTED = 'UNSORTED',
}

const data: dataTypes[] = [
  {
    name: 'picture shelf',
    creationDate: '11/8/1991',
    id: 2,
  },
  { name: 'documents', creationDate: '11/1/2001', id: 1 },
  { name: 'videos', creationDate: '11/4/2021', id: 3 },
];
const headers: headerTypes[] = [
  { header: 'Name', key: 'name' },
  { header: 'Creation date', key: 'creationDate' },
  { header: 'Id', key: 'id' },
];

export const Table = ({ mulitSelect }: { mulitSelect?: boolean }) => {
  const [selectAll, setSelectAll] = useState<boolean>(false);
  const [selectedRows, setSelectedRows] = useState<any>([]);
  const [sortingDirections, setSortingDirections] = useState<any>({});
  const [tableData, setTableData] = useState(data);

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

  return (
    <StyledTable>
      <Thead>
        <tr>
          {mulitSelect && (
            <StyledTableheader>
              <CheckBox onChange={handleSelectAll} checked={selectAll} />
            </StyledTableheader>
          )}
          {headers.map(({ header, key }, idx) => (
            <StyledTableheader
              onClick={() =>
                sortColumn(
                  key,
                  sortingDirections,
                  tableData,
                  setTableData,
                  setSortingDirections
                )
              }
              key={`${header}-${idx}`}
            >
              {header}
              <ArrowContainer
                style={{ display: 'flex', alignSelf: 'flex-end' }}
              >
                {sorterArrowToggle(key)}
              </ArrowContainer>
            </StyledTableheader>
          ))}
          {!mulitSelect && <StyledTableheader>Actions</StyledTableheader>}
        </tr>
      </Thead>
      <tbody>
        {tableData.map((item, idx) => (
          <Row
            key={`${item.name}-${idx}`}
            selectedRows={selectedRows}
            setSelectedRows={setSelectedRows}
            multiSelect={mulitSelect}
            data={item}
            isChecked={selectedRows.some(
              (rowData: any) => rowData.id === item.id
            )}
          />
        ))}
      </tbody>
    </StyledTable>
  );
};
