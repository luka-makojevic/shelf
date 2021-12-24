import { useEffect, useState } from 'react';
import { FaTrash } from 'react-icons/fa';
import { Table } from '../../components/table/table';
import TableWrapper from '../../components/table/tableWrapper';
import {
  ActionsBox,
  ButtonActionsBox,
} from '../../components/table/tableWrapper.styles';
import { Button } from '../../components/UI/button';
import SearchBar from '../../components/UI/searchBar/searchBar';
import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { FileDataType, TableDataTypes } from '../../interfaces/dataTypes';
import { Description } from '../../components/text/text-styles';
import Breadcrumbs from '../../components/breadcrumbs';
import Modal from '../../components/modal';
import DeleteShelfModal from '../../components/modal/deleteMessageModal';
import fileServices from '../../services/fileServices';
import { useShelf } from '../../hooks/shelfHooks';
import { useAppSelector } from '../../store/hooks';

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
  const trashData = useAppSelector((state) => state.trash.trashData);
  const [filteredData, setFilteredData] = useState<TableDataTypes[]>([]);
  const [selectedRows, setSelectedRows] = useState<TableDataTypes[]>([]);
  const [tableData, setTableData] = useState<TableDataTypes[]>([]);
  const [openModal, setOpenModal] = useState(false);
  const [error, setError] = useState('');
  const message =
    trashData.length === 0 // replace data with trashData
      ? 'Trash is empty'
      : 'Sorry, no matching results found :(';

  const { getTrash } = useShelf();

  useEffect(() => {
    getTrash(
      () => {},
      (err) => {
        setError(err);
      }
    );
  }, []);

  useEffect(() => {
    const newData = trashData.map((item) => ({
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

  const getSelectedRows = (selectedRowsData: TableDataTypes[]) => {
    setSelectedRows(selectedRowsData);
  };

  const handleHardDelete = () => {
    setOpenModal(true);
  };

  const handleRecoverFromTrash = (row: TableDataTypes) => {
    if (row.folder) {
      fileServices
        .recoverFolderFromTrash(row.id)
        .then(() =>
          getTrash(
            () => {},
            () => {}
          )
        )
        .catch((err) => {
          if (err.response?.status === 500) {
            setError('Internal server error');
            return;
          }
          setError(err.response?.data?.message);
        });
    } else {
      fileServices
        .recoverFileFromTrash(row.id)
        .then(() =>
          getTrash(
            () => {},
            () => {}
          )
        )
        .catch((err) => {
          if (err.response?.status === 500) {
            setError('Internal server error');
            return;
          }
          setError(err.response?.data?.message);
        });
    }
  };

  const handleSetError = () => {
    setError('');
  };

  const handleModalClose = () => {
    setOpenModal(false);
    setSelectedRows([]);
  };

  return (
    <>
      {openModal && (
        <Modal title="Delete shelf" onCloseModal={handleModalClose}>
          <DeleteShelfModal
            onCloseModal={handleModalClose}
            onError={setError}
            message="This action will permanently delete this file/folder"
            selectedData={selectedRows}
          />
        </Modal>
      )}
      {error && (
        <AlertPortal
          type={AlertMessage.ERRROR}
          title="Error"
          message={error}
          onClose={handleSetError}
        />
      )}
      <TableWrapper title="Trash">
        <Breadcrumbs />
        <ActionsBox>
          <SearchBar
            placeholder="Search..."
            data={tableData}
            setData={setFilteredData}
            searchKey="name"
          />
          <ButtonActionsBox>
            <Button
              onClick={handleHardDelete}
              icon={<FaTrash />}
              disabled={selectedRows.length === 0}
            >
              Delete
            </Button>
          </ButtonActionsBox>
        </ActionsBox>
        {trashData.length === 0 || filteredData.length === 0 ? ( // replace data with trashData
          <Description>{message}</Description>
        ) : (
          <Table
            setTableData={setFilteredData}
            data={filteredData}
            headers={headers}
            path="folders/"
            location="trash"
            mulitSelect
            getSelectedRows={getSelectedRows}
            onRecoverFromTrash={handleRecoverFromTrash}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Trash;
