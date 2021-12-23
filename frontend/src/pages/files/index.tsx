import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  FaCloudDownloadAlt,
  FaCloudUploadAlt,
  FaPlusCircle,
  FaTrash,
} from 'react-icons/fa';
import { Table } from '../../components/table/table';
import TableWrapper from '../../components/table/TableWrapper';
import { TableDataTypes } from '../../interfaces/dataTypes';
import { useAppSelector } from '../../store/hooks';
import AlertPortal from '../../components/alert/alert';
import Modal from '../../components/modal';
import AddFileModal from '../../components/modal/addFileModal';
import { Button } from '../../components/UI/button';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { Description } from '../../components/text/text-styles';
import Breadcrumbs from '../../components/breadcrumbs';
import { ButtonContainer } from '../../components/table/tableWrapper-styles';
import SearchBar from '../../components/UI/searchBar/searchBar';
import { useFiles } from '../../hooks/fileHooks';
import FolderModal from '../../components/modal/folderModal';

const Files = () => {
  const files = useAppSelector((state) => state.file.files);
  const loading = useAppSelector((state) => state.loading.loading);
  const [openCreateFileModal, setOpenCreateFileModal] = useState(false);
  const [openUploadModal, setOpenUploadModal] = useState(false);
  const [error, setError] = useState('');

  const handleOpenCreateFileModal = () => {
    setOpenCreateFileModal(true);
  };
  const handleOpenUploadModal = () => {
    setOpenUploadModal(true);
  };

  const handleSetError = () => {
    setError('');
  };

  const { getShelfFiles, getFolderFiles } = useFiles();

  const [filesforTable, setFilesForTable] = useState<TableDataTypes[]>([]);
  const [filteredFiles, setFilteredFiles] = useState<TableDataTypes[]>([]);
  const [selectedRows, setSelectedRows] = useState<TableDataTypes[]>([]);
  const { shelfId, folderId } = useParams();

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

  const message =
    files?.length === 0
      ? 'You dont have any files to display'
      : 'Sorry, no matching results found';

  const headers = [
    { header: 'Name', key: 'name' },
    { header: 'Creation date', key: 'createdAt' },
  ];

  const getSelectedRows = (selectedRowsData: TableDataTypes[]) => {
    setSelectedRows(selectedRowsData);
  };
  if (loading) return null;
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
      {openCreateFileModal && (
        <Modal
          title="Create folder"
          onCloseModal={setOpenCreateFileModal}
          closeIcon
        >
          <FolderModal
            onCloseModal={setOpenCreateFileModal}
            onError={setError}
            shelfId={shelfId}
            folderId={folderId}
            placeholder="Folder name"
            buttonText="Create"
          />
        </Modal>
      )}
      {openUploadModal && (
        <Modal title="Upload files" onCloseModal={setOpenUploadModal} closeIcon>
          <AddFileModal onCloseModal={setOpenUploadModal} />
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
          <Button onClick={handleOpenCreateFileModal} icon={<FaPlusCircle />}>
            Create folder
          </Button>
          <Button icon={<FaCloudDownloadAlt />}>Download</Button>
          <Button onClick={handleOpenUploadModal} icon={<FaCloudUploadAlt />}>
            Upload
          </Button>
          <Button icon={<FaTrash />}>Delete</Button>
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
          />
        )}
      </TableWrapper>
    </>
  );
};

export default Files;
