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
import DeleteModal from '../../components/modal/deleteModal';
import Breadcrumbs from '../../components/breadcrumbs';
import fileServices from '../../services/fileServices';
import trashService from '../../services/trashService';
import folderService from '../../services/folderService';
import TableWrapper from '../../components/table/TableWrapper';
import { ActionType, Recover } from '../../components/table/table.interfaces';

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

  const handleRecoverFromTrash = (row: TableDataTypes) => {
    if (row.folder) {
      folderService
        .recoverFolderFromTrash([row.id])
        .then((res) => {
          getData();
          toast.success(res.data.message);
        })
        .catch((err) => toast.error(err));
    } else {
      fileServices
        .recoverFileFromTrash([row.id])
        .then((res) => {
          getData();
          toast.success(res.data.message);
        })
        .catch((err) => toast.error(err));
    }
  };

  const handleModalClose = () => {
    setOpenModal(false);
    setSelectedRows([]);
  };

  const handleHardDelete = () => {
    const fileIds: number[] = [];
    const folderIds: number[] = [];

    selectedRows.forEach((item) => {
      if (item.folder) folderIds.push(item.id);
      else fileIds.push(item.id);
    });

    if (fileIds.length > 0) {
      fileServices
        .hardDeleteFile(fileIds)
        .then((res) => {
          getData();
          toast.success(res.data.message);
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
    }

    if (folderIds.length > 0) {
      folderService
        .hardDeleteFolder(folderIds)
        .then((res) => {
          getData();
          toast.success(res.data.message);
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
    }
    handleModalClose();
  };

  if (isLoading) return null;

  const actions: ActionType[] = [
    { comp: Recover, handler: handleRecoverFromTrash, key: 1 },
  ];

  return (
    <>
      {openModal && (
        <DeleteModal
          title="Delete from trash"
          onDelete={handleHardDelete}
          onCloseModal={handleModalClose}
          message="This action will permanently delete selected items!"
        />
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
            actions={actions}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Trash;
