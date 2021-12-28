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
import TableWrapper from '../../components/table/TableWrapper';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { useAppSelector } from '../../store/hooks';
import Modal from '../../components/modal';
import UploadModal from '../../components/modal/uploadModal';
import { Button } from '../../components/UI/button';
import { Description } from '../../components/text/text-styles';
import Breadcrumbs from '../../components/breadcrumbs';
import { ButtonContainer } from '../../components/table/tableWrapper-styles';
import SearchBar from '../../components/UI/searchBar/searchBar';
import { useFiles } from '../../hooks/fileHooks';
import FolderModal from '../../components/modal/folderModal';
import fileServices from '../../services/fileServices';

const Files = () => {
  const files = useAppSelector((state) => state.file.files);
  const loading = useAppSelector((state) => state.loading.loading);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [openModal, setOpenModal] = useState(false);
  const [openUploadModal, setOpenUploadModal] = useState(false);
  const [filesforTable, setFilesForTable] = useState<TableDataTypes[]>([]);
  const [filteredFiles, setFilteredFiles] = useState<TableDataTypes[]>([]);
  const [selectedRows, setSelectedRows] = useState<TableDataTypes[]>([]);
  const [selectedFile, setSelectedFile] = useState<TableDataTypes | null>();
  const { shelfId, folderId } = useParams();

  const handleOpenUploadModal = () => {
    setOpenUploadModal(true);
  };

  const { getShelfFiles, getFolderFiles } = useFiles();
  const getData = () => {
    setIsLoading(true);
    if (folderId) {
      getFolderFiles(
        Number(folderId),
        () => {
          setIsLoading(false);
        },
        () => {
          setIsLoading(false);
        }
      );
    } else
      getShelfFiles(
        Number(shelfId),
        () => {
          setIsLoading(false);
        },
        () => {
          setIsLoading(false);
        }
      );
  };

  useEffect(() => {
    getData();
  }, [folderId, shelfId]);

  useEffect(() => {
    if (files) {
      const newFiles = files.map((file) => ({
        name: file.name,
        createdAt: new Date(file.createdAt).toLocaleDateString('en-US'),
        id: file.id,
        folder: file.folder ? 1 : 0,
      }));
      setFilteredFiles(newFiles);
      setFilesForTable(newFiles);
    }
  }, [files]);

  const message =
    files?.length === 0
      ? 'You dont have any files to display'
      : 'Sorry, no matching results found';

  const headers = [
    { header: 'Name', key: 'name' },
    { header: 'Creation date', key: 'createdAt' },
  ];

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
      fileServices
        .softDeleteFolder(folderIds)
        .then(() => {
          toast.success('Folders moved to trash');

          getData();
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
  };
  const handleEdit = (file: TableDataTypes) => {
    setSelectedFile(file);
    setOpenModal(true);
  };

  const handleCreateFolder = () => {
    setOpenModal(true);
  };
  const handleModalClose = () => {
    setSelectedFile(null);
    setOpenModal(false);
  };

  const getSelectedRows = (selectedRowsData: TableDataTypes[]) => {
    setSelectedRows(selectedRowsData);
  };
  const handleDownload = () => {
    if (selectedRows.length === 0) return;
    fileServices
      .downloadFile(10)
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

  if (isLoading) return null;

  return (
    <>
      {openModal && (
        <Modal
          title={selectedFile ? 'Edit name' : 'Create folder'}
          onCloseModal={handleModalClose}
          closeIcon
        >
          <FolderModal
            onCloseModal={handleModalClose}
            shelfId={shelfId}
            folderId={folderId}
            getData={getData}
            placeholder={selectedFile ? '' : 'Folder Name'}
            buttonText={selectedFile ? 'Rename' : 'Create'}
            file={selectedFile}
          />
        </Modal>
      )}
      {openUploadModal && (
        <Modal title="Upload files" onCloseModal={setOpenUploadModal} closeIcon>
          <UploadModal onCloseModal={setOpenUploadModal} />
        </Modal>
      )}

      <TableWrapper>
        <Breadcrumbs />
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
            onEdit={handleEdit}
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Files;
