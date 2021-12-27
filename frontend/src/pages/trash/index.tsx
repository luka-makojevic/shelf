import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { FaTrash } from 'react-icons/fa';
import { Table } from '../../components/table/table';
import {
  ActionsBox,
  ButtonActionsBox,
} from '../../components/table/tableWrapper.styles';
import { Button } from '../../components/UI/button';
import SearchBar from '../../components/UI/searchBar/searchBar';
import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { Description } from '../../components/text/text-styles';
import Breadcrumbs from '../../components/breadcrumbs';
import Modal from '../../components/modal';
import DeleteShelfModal from '../../components/modal/deleteMessageModal';
import fileServices from '../../services/fileServices';
import { useShelf } from '../../hooks/shelfHooks';
import { useAppSelector } from '../../store/hooks';
import TableWrapper from '../../components/table/TableWrapper';

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

const Trash = () => {
  const trashData = useAppSelector((state) => state.trash.trashData);
  const [filteredData, setFilteredData] = useState<TableDataTypes[]>([]);
  const [selectedRows, setSelectedRows] = useState<TableDataTypes[]>([]);
  const [tableData, setTableData] = useState<TableDataTypes[]>([]);
  const [openModal, setOpenModal] = useState(false);
  const [error, setError] = useState('');
  const { folderId } = useParams();

  const message =
    trashData.length === 0
      ? 'Trash is empty'
      : 'Sorry, no matching results found :(';

  const { getTrash, getTrashFolders } = useShelf();

  useEffect(() => {
    if (folderId) {
      getTrashFolders(
        Number(folderId),
        () => {},
        (err) => {
          setError(err);
        }
      );
    } else {
      getTrash(
        () => {},
        (err) => {
          setError(err);
        }
      );
    }
  }, [folderId]);

  useEffect(() => {
    const newData = trashData.map((item) => ({
      id: item.id,
      folder: item.folder ? 1 : 0,
      name: item.name,
      size: item.size,
      createdAt: new Date(item.createdAt).toLocaleDateString('en-US'),
    }));

    setFilteredData(newData);
    setTableData(newData);
  }, [trashData]);

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
        {(trashData && trashData.length === 0) || filteredData.length === 0 ? (
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
