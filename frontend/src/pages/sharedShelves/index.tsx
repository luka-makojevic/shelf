import { useEffect, useState } from 'react';
import { Table } from '../../components/table/table';
import TableWrapper from '../../components/table/TableWrapper';
import { useShelf } from '../../hooks/shelfHooks';
import {
  SharedFilesDataType,
  TableDataTypes,
} from '../../interfaces/dataTypes';
import { useAppSelector } from '../../store/hooks';
import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { Description } from '../../components/text/text-styles';
import SearchBar from '../../components/UI/searchBar/searchBar';

const headers = [
  { header: 'Name', key: 'name' },
  { header: 'Shared by', key: 'sharedBy' }, // check with be name of column
  { header: 'Share date', key: 'createdAt' },
];

const data: SharedFilesDataType[] = [
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
    sharedBy: 142,
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
    sharedBy: 142,
  },
  {
    name: 'Folder 3',
    createdAt: '2021-12-20T11:05:17.523Z',
    folder: true,
    id: 3,
    size: 33,
    isDeleted: true,
    userId: 40,
    shelfId: 1,
    parentFolderId: 2,
    path: '',
    sharedBy: 142,
  },
];

const SharedWithMe = () => {
  const sharedData = useAppSelector((state) => state.shared.data);
  const user = useAppSelector((state) => state.user.user);
  const [sharedDataForTable, setSharedDataForTable] = useState<
    TableDataTypes[]
  >([]);
  const [filteredData, setFilteredData] = useState<TableDataTypes[]>([]);
  const [error, setError] = useState('');

  const handleSetError = () => {
    setError('');
  };

  const { getShelves } = useShelf();

  useEffect(() => {
    getShelves(
      { userId: user?.id },
      () => {},
      (err: string) => {
        setError(err);
      }
    );
  }, []);

  useEffect(() => {
    const newShelves = data.map((item) => ({
      name: item.name,
      sharedBy: item.sharedBy,
      createdAt: new Date(item.createdAt).toLocaleDateString('en-US'),
      id: item.id,
    }));

    setSharedDataForTable(newShelves);
    setFilteredData(newShelves);
  }, [sharedData]);

  const handleDelete = () => {};

  const handleEdit = () => {};

  const message =
    data.length === 0
      ? 'No data was shared with you'
      : 'Sorry, no matching results found';

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

      <TableWrapper title="Shared with Me">
        <SearchBar
          placeholder="Search..."
          data={sharedDataForTable}
          setData={setFilteredData}
          searchKey="name"
        />
        {data.length === 0 || filteredData.length === 0 ? (
          <Description>{message}</Description>
        ) : (
          <Table
            setTableData={setFilteredData}
            data={filteredData}
            headers={headers}
            path="shared/"
            onDelete={handleDelete}
            onEdit={handleEdit}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default SharedWithMe;
