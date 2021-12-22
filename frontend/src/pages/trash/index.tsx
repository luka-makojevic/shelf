import { useEffect, useState } from 'react';
import { Table } from '../../components/table/table';
import TableWrapper from '../../components/table/TableWrapper';
import {
  ActionsBox,
  ButtonActionsBox,
} from '../../components/table/tableWrapper.styles';
import { Button } from '../../components/UI/button';
import SearchBar from '../../components/UI/searchBar/searchBar';
import { useTrash } from '../../hooks/trashHook';
import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { FileDataType, TableDataTypes } from '../../interfaces/dataTypes';
// import { useAppSelector } from '../../store/hooks';
import { Description } from '../../components/text/text-styles';

const headers = [
  {
    header: 'Name',
    key: 'name',
  },
  {
    header: 'Size',
    key: 'size',
  },
  {
    header: 'Creation Date',
    key: 'createdAt',
  },
];

// DUMMY DATA - remove once get trash data from DB is done
const data: FileDataType[] = [
  {
    name: 'Folder 1',
    createdAt: '2021-12-20T11:05:17.523Z',
    folder: false,
    id: 1,
    size: 33,
    isDeleted: true,
    userId: 40,
    shelfId: 1,
    parentFolderId: 2,
    path: '',
  },
  {
    name: 'Folder 2',
    createdAt: '2021-12-20T11:05:17.523Z',
    folder: true,
    id: 2,
    size: 33,
    isDeleted: true,
    userId: 40,
    shelfId: 1,
    parentFolderId: 2,
    path: '',
  },
  {
    name: 'Folder 63',
    createdAt: '2021-12-20T11:05:17.523Z',
    folder: true,
    id: 3,
    size: 33,
    isDeleted: true,
    userId: 40,
    shelfId: 1,
    parentFolderId: 2,
    path: '',
  },
];

const Trash = () => {
  // const trashData = useAppSelector((state) => state.trash.trashData);
  const [filteredData, setFilteredData] = useState<TableDataTypes[]>([]);
  const [tableData, setTableData] = useState<TableDataTypes[]>([]);
  const [error, setError] = useState('');
  const message =
    data.length === 0 // replace data with trashData
      ? 'Trash is empty'
      : 'Sorry, no matching results found :(';

  const { getTrash } = useTrash();

  useEffect(() => {
    getTrash(
      () => {},
      (err) => {
        setError(err);
      }
    );
  }, []);

  useEffect(() => {
    const newData = data.map((item) => ({
      // replace data with trashData
      id: item.id,
      folder: item.folder ? 1 : 0,
      name: item.name,
      size: item.size,
      createdAt: new Date(item.createdAt).toLocaleDateString('en-US'),
    }));

    setFilteredData(newData);
    setTableData(newData);
  }, [data]);

  const handleEmptyTrash = () => {};

  const handleDelete = () => {};

  const handleEdit = () => {};

  const handleSetError = () => {
    setError('');
  };

  return (
    <>
      {error && (
        <AlertPortal
          type={AlertMessage.ERRROR}
          title="Error"
          message={error}
          onClose={handleSetError}
        />
      )}
      <TableWrapper title="Trash" description="All deleted files">
        <ActionsBox>
          <SearchBar
            placeholder="Search..."
            data={tableData}
            setData={setFilteredData}
            searchKey="name"
          />
          <ButtonActionsBox>
            <Button onClick={handleEmptyTrash}>Empty Trash</Button>
          </ButtonActionsBox>
        </ActionsBox>
        {data.length === 0 || filteredData.length === 0 ? ( // replace data with trashData
          <Description>{message}</Description>
        ) : (
          <Table
            setTableData={setFilteredData}
            data={filteredData}
            headers={headers}
            path="trash/"
            onDelete={handleDelete}
            onEdit={handleEdit}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Trash;
