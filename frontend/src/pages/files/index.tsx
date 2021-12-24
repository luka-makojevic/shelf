import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  FaCloudDownloadAlt,
  FaCloudUploadAlt,
  FaPlusCircle,
  FaTrash,
} from 'react-icons/fa';
import { useDispatch } from 'react-redux';
import { Table } from '../../components/table/table';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { useAppSelector } from '../../store/hooks';
import AlertPortal from '../../components/alert/alert';
import Modal from '../../components/modal';
import UploadModal from '../../components/modal/uploadModal';
import { Button } from '../../components/UI/button';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { Description } from '../../components/text/text-styles';
import Breadcrumbs from '../../components/breadcrumbs';
import { ButtonContainer } from '../../components/table/tableWrapper.styles';
import SearchBar from '../../components/UI/searchBar/searchBar';
import { useFiles } from '../../hooks/fileHooks';
import FolderModal from '../../components/modal/folderModal';
import { setPathHistory } from '../../store/pathHistory';
import fileServices from '../../services/fileServices';
import TableWrapper from '../../components/table/TableWrapper';

const Files = () => {
  const files = useAppSelector((state) => state.file.files);
  const loading = useAppSelector((state) => state.loading.loading);
  const [openModal, setOpenModal] = useState(false);
  const [openUploadModal, setOpenUploadModal] = useState(false);
  const [error, setError] = useState('');
  const [filesforTable, setFilesForTable] = useState<TableDataTypes[]>([]);
  const [filteredFiles, setFilteredFiles] = useState<TableDataTypes[]>([]);
  const [selectedRows, setSelectedRows] = useState<TableDataTypes[]>([]);
  const [selectedFile, setSelectedFile] = useState<TableDataTypes | null>();
  const [success, setSuccess] = useState('');
  const { shelfId, folderId } = useParams();

  const dispatch = useDispatch();

  const handleOpenUploadModal = () => {
    setOpenUploadModal(true);
  };

  const handleAlertClose = () => {
    setError('');
    setSuccess('');
  };

  const { getShelfFiles, getFolderFiles } = useFiles();
  const getData = () => {
    if (folderId) {
      getFolderFiles(
        Number(folderId),
        () => {},
        () => {}
      );
    } else
      getShelfFiles(
        Number(shelfId),
        () => {},
        () => {}
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

  useEffect(
    () => () => {
      dispatch(setPathHistory([]));
    },
    []
  );

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
        .then(() => getData())
        .catch((err) => {
          if (err.response?.status === 500) {
            setError('Internal server error');
          } else setError('File could not be deleted');
        });
    if (folderIds.length !== 0)
      fileServices
        .softDeleteFolder(folderIds)
        .then(() => getData())
        .catch((err) => {
          if (err.response?.status === 500) {
            setError('Internal server error');
          } else setError('Folder could not be deleted');
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
        setError('Failed to download');
      });
  };

  if (loading) return null;
  return (
    <>
      {(error || success) && (
        <AlertPortal
          type={error ? AlertMessage.ERRROR : AlertMessage.SUCCESS}
          title={`${error ? 'Error' : 'Success'}`}
          message={error || success}
          onClose={handleAlertClose}
        />
      )}
      {openModal && (
        <Modal
          title={selectedFile ? 'Edit name' : 'Create folder'}
          onCloseModal={handleModalClose}
          closeIcon
        >
          <FolderModal
            onCloseModal={handleModalClose}
            onError={setError}
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
          <UploadModal
            onCloseModal={setOpenUploadModal}
            onError={setError}
            onSuccess={setSuccess}
          />
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
