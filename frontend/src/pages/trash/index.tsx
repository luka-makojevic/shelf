import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { FaTrash } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { Table } from '../../components/table/table';
import {
  ActionsBox,
  ButtonActionsBox,
} from '../../components/table/tableWrapper.styles';
import { Button } from '../../components/UI/button';
import SearchBar from '../../components/UI/searchBar/searchBar';
import {
  FileDataType,
  PathHistoryData,
  TableDataTypes,
} from '../../interfaces/dataTypes';
import { Description } from '../../components/text/text-styles';
import Breadcrumbs from '../../components/breadcrumbs';
import Modal from '../../components/modal';
import DeleteModal from '../../components/modal/deleteModal';
import fileServices from '../../services/fileServices';
import TableWrapper from '../../components/table/tableWrapper';
import trashService from '../../services/trashService';
import folderService from '../../services/folderService';

const headers = [
  {
    header: 'Name',
    key: 'name',
  },
  {
    header: 'Creation Date',
    key: 'createdAt',
  },
];

const Trash = () => {
  const [trash, setTrash] = useState<FileDataType[]>([]);
  const [filteredData, setFilteredData] = useState<TableDataTypes[]>([]);
  const [selectedRows, setSelectedRows] = useState<TableDataTypes[]>([]);
  const [tableData, setTableData] = useState<TableDataTypes[]>([]);
  const [openModal, setOpenModal] = useState(false);
  const { folderId } = useParams();
  const [isLoading, setIsLoading] = useState(false);
  const [pathHistory, setPathHistory] = useState<PathHistoryData[]>([]);

  const setMessage = () => {
    if (trash.length === 0 && pathHistory.length === 0) return 'Trash is empty';
    if (pathHistory.length > 0) return 'There are no further folders or files';
    return 'Sorry, no matching results found';
  };
  const message = setMessage();

  const getData = () => {
    setIsLoading(true);
    if (folderId) {
      trashService
        .getTrashFiles(Number(folderId))
        .then((res) => {
          setPathHistory(res.data.breadCrumbs);
          setTrash(res.data.shelfItems);
        })
        .catch((err) => toast.error(err))
        .finally(() => setIsLoading(false));
    } else {
      trashService
        .getTrash()
        .then((res) => {
          setTrash(res.data.shelfItems);
        })
        .catch((err) => toast.error(err.response?.data?.message))
        .finally(() => setIsLoading(false));
    }
  };

  useEffect(() => {
    getData();
    setPathHistory([]);
  }, [folderId]);

  useEffect(() => {
    if (trash) {
      const newData = trash.map((item) => ({
        id: item.id,
        folder: item.folder ? 1 : 0,
        name: item.name,
        createdAt: new Date(item.createdAt).toLocaleString('en-US'),
      }));

      setFilteredData(newData);
      setTableData(newData);
    }
  }, [trash]);

  const getSelectedRows = (selectedRowsData: TableDataTypes[]) => {
    setSelectedRows(selectedRowsData);
  };

  const handleOpenDeleteModal = () => {
    if (selectedRows.length === 0) return;
    setOpenModal(true);
  };

  const handleHardDelete = () => {
    getData();
  };

  const handleRecoverFromTrash = (row: TableDataTypes) => {
    if (row.folder) {
      folderService
        .recoverFolderFromTrash([row.id])
        .then(() => {
          getData();
          toast.success('Folder recovered from trash');
        })
        .catch((err) => toast.error(err));
    } else {
      fileServices
        .recoverFileFromTrash([row.id])
        .then(() => {
          getData();
          toast.success('File recovered from trash');
        })
        .catch((err) => toast.error(err));
    }
  };

  const handleModalClose = () => {
    setOpenModal(false);
    setSelectedRows([]);
  };

  if (isLoading) return null;

  return (
    <>
      {openModal && (
        <Modal title="Delete from trash" onCloseModal={handleModalClose}>
          <DeleteModal
            onDeleteFiles={handleHardDelete}
            onCloseModal={handleModalClose}
            message="This action will permanently delete selected items"
            selectedData={selectedRows}
          />
        </Modal>
      )}

      <TableWrapper title="Trash">
        <Breadcrumbs pathHistory={pathHistory} />
        <ActionsBox>
          <SearchBar
            placeholder="Search..."
            data={tableData}
            setData={setFilteredData}
            searchKey="name"
          />
          <ButtonActionsBox>
            <Button onClick={handleOpenDeleteModal} icon={<FaTrash />}>
              Delete
            </Button>
          </ButtonActionsBox>
        </ActionsBox>
        {(trash && trash.length === 0) || filteredData.length === 0 ? (
          <Description>{message}</Description>
        ) : (
          <Table
            setTableData={setFilteredData}
            data={filteredData}
            headers={headers}
            path="folders/"
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
