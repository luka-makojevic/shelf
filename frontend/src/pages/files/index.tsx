import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  FaCloudDownloadAlt,
  FaCloudUploadAlt,
  FaPlusCircle,
  FaTrash,
} from 'react-icons/fa';
import { toast } from 'react-toastify';
import { Table } from '../../components/table/table';
import TableWrapper from '../../components/table/tableWrapper';
import {
  FileDataType,
  PathHistoryData,
  TableDataTypes,
} from '../../interfaces/dataTypes';
import Modal from '../../components/modal';
import UploadModal from '../../components/modal/uploadModal';
import { Button } from '../../components/UI/button';
import { Description } from '../../components/text/text-styles';
import Breadcrumbs from '../../components/breadcrumbs';
import { ButtonContainer } from '../../components/table/tableWrapper.styles';
import SearchBar from '../../components/UI/searchBar/searchBar';
import FolderModal from '../../components/modal/folderModal';
import fileServices from '../../services/fileServices';
import folderService from '../../services/folderService';

const headers = [
  { header: 'Name', key: 'name' },
  { header: 'Creation date', key: 'createdAt' },
];

const Files = () => {
  const [files, setFiles] = useState<FileDataType[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [openEditModal, setOpenEditModal] = useState(false);
  const [openUploadModal, setOpenUploadModal] = useState(false);
  const [filesforTable, setFilesForTable] = useState<TableDataTypes[]>([]);
  const [filteredFiles, setFilteredFiles] = useState<TableDataTypes[]>([]);
  const [selectedRows, setSelectedRows] = useState<TableDataTypes[]>([]);
  const [selectedFile, setSelectedFile] = useState<TableDataTypes | null>();
  const { shelfId, folderId } = useParams();
  const [pathHistory, setPathHistory] = useState<PathHistoryData[]>([]);
  const message =
    files?.length === 0
      ? 'You dont have any files to display'
      : 'Sorry, no matching results found';

  const getData = () => {
    setIsLoading(true);
    if (folderId) {
      folderService
        .getFolderFiles(Number(folderId))
        .then((res) => {
          setPathHistory(res.data.breadCrumbs);
          setFiles(res.data.shelfItems);
        })
        .catch((err) => toast.error(err))
        .finally(() => setIsLoading(false));
    } else
      fileServices
        .getShelfFiles(Number(shelfId))
        .then((res) => {
          setPathHistory(res.data.breadCrumbs);
          setFiles(res.data.shelfItems);
        })
        .catch((err) => toast.error(err))
        .finally(() => setIsLoading(false));
  };

  useEffect(() => {
    getData();
  }, [folderId, shelfId]);

  useEffect(() => {
    if (files) {
      const newFiles = files.map((file) => ({
        name: file.name,
        createdAt: new Date(file.createdAt).toLocaleString('en-US'),
        id: file.id,
        folder: file.folder ? 1 : 0,
      }));
      setFilteredFiles(newFiles);
      setFilesForTable(newFiles);
    }
  }, [files]);

  const getSelectedRows = (selectedRowsData: TableDataTypes[]) => {
    setSelectedRows(selectedRowsData);
  };

  const handleDelete = () => {
    const fileIds: number[] = [];
    const folderIds: number[] = [];
    selectedRows.forEach((item) => {
      if (item.folder) folderIds.push(item.id);
      else {
        fileIds.push(item.id);
      }
    });

    if (fileIds.length !== 0)
      fileServices
        .softDeleteFile(fileIds)
        .then(() => {
          toast.success('Files moved to trash');
          getData();
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });

    if (folderIds.length !== 0)
      folderService
        .softDeleteFolder(folderIds)
        .then(() => {
          toast.success('Folders moved to trash');
          getData();
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
  };

  const handleEdit = (file: TableDataTypes, newName: string) => {
    const newFiles = files.map((item) => {
      if (item.id === file.id) {
        if (!file.folder) {
          const extension = item.name.substring(item.name.lastIndexOf('.'));
          return { ...item, name: newName + extension };
        }
        return { ...item, name: newName };
      }
      return item;
    });
    setFiles(newFiles);
  };

  const handleCreateFolder = () => {
    setOpenEditModal(true);
  };

  const handleDownload = () => {
    if (selectedRows.length === 0) return;
    fileServices
      .downloadFile(selectedRows[0].id)
      .then((res) => {
        const url = window.URL.createObjectURL(new Blob([res.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', selectedRows[0].name);
        document.body.appendChild(link);
        link.click();
        link.remove();
      })
      .catch(() => {
        toast.error('Failed to download');
      });
  };

  const handleOpenUploadModal = () => {
    setOpenUploadModal(true);
  };
  const handleCloseUploadModal = () => {
    setOpenUploadModal(false);
  };
  const handleEditModalClose = () => {
    setSelectedFile(null);
    setOpenEditModal(false);
  };
  const handleOpenEditModal = (file: TableDataTypes) => {
    setSelectedFile(file);
    setOpenEditModal(true);
  };

  if (isLoading) return null;

  return (
    <>
      {openEditModal && (
        <Modal
          title={selectedFile ? 'Edit name' : 'Create folder'}
          onCloseModal={handleEditModalClose}
          closeIcon
        >
          <FolderModal
            onEdit={handleEdit}
            onCloseModal={handleEditModalClose}
            shelfId={shelfId}
            folderId={folderId}
            onGetData={getData}
            placeholder={selectedFile ? '' : 'Folder Name'}
            buttonText={selectedFile ? 'Rename' : 'Create'}
            file={selectedFile}
          />
        </Modal>
      )}
      {openUploadModal && (
        <Modal
          title="Upload files"
          onCloseModal={handleCloseUploadModal}
          closeIcon
        >
          <UploadModal
            onCloseModal={handleCloseUploadModal}
            onGetData={getData}
          />
        </Modal>
      )}

      <TableWrapper>
        <Breadcrumbs pathHistory={pathHistory} />
        <SearchBar
          placeholder="Search..."
          data={filesforTable}
          setData={setFilteredFiles}
          searchKey="name"
        />
        <ButtonContainer>
          <Button onClick={handleCreateFolder} icon={<FaPlusCircle />}>
            Create folder
          </Button>
          <Button icon={<FaCloudDownloadAlt />} onClick={handleDownload}>
            Download
          </Button>
          <Button onClick={handleOpenUploadModal} icon={<FaCloudUploadAlt />}>
            Upload
          </Button>
          <Button icon={<FaTrash />} onClick={handleDelete}>
            Delete
          </Button>
        </ButtonContainer>
        {(files && files.length === 0) || filteredFiles.length === 0 ? (
          <Description>{message}</Description>
        ) : (
          <Table
            mulitSelect
            setTableData={setFilteredFiles}
            data={filteredFiles}
            headers={headers}
            path="folders/"
            getSelectedRows={getSelectedRows}
            onEdit={handleOpenEditModal}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Files;
