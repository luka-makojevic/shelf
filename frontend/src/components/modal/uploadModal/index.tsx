import { ChangeEvent, DragEvent, useEffect, useState } from 'react';
import { FaFolderOpen, FaTimes } from 'react-icons/fa';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import fileServices from '../../../services/fileServices';
import { theme } from '../../../theme';
import { ModalButtonDivider } from '../../layout/layout.styles';
import ProgressBar from '../../progressBar';
import { Button } from '../../UI/button';
import { Footer } from '../modal.styles';
import { UploadModalProps } from './uploadModal.interfaces';
import {
  AddedFilesIconButton,
  AddedFilesList,
  AddedFilesListItem,
  AddFilesInput,
  AddFilesLabel,
  AddFilesText,
  DropZoneWrapper,
} from './uploadModal.styles';

const UploadModal = ({ onCloseModal, onGetData }: UploadModalProps) => {
  const [isDragOver, setIsDragOver] = useState(false);
  const [filesForUpload, setFilesForUpload] = useState<File[]>([]);
  const [progress, setProgress] = useState(0);
  const { shelfId, folderId } = useParams();
  const [abortController] = useState<AbortController>(new AbortController());

  const addFilesForUpload = (files: FileList) => {
    const filesArray = Array.from(files);

    filesArray.forEach((file) => {
      const isFileAlreadyAdded = filesForUpload.some(
        (addedFile) => addedFile.name === file.name
      );

      if (isFileAlreadyAdded) return;

      setFilesForUpload((prevState: File[]) => [...prevState, file]);
    });
  };

  const handleAddFiles = (e: ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files) return;
    addFilesForUpload(e.target.files);
  };

  const handleDragOver = (e: DragEvent) => {
    e.preventDefault();
    setIsDragOver(true);
  };

  const handleDragLeave = (e: DragEvent) => {
    e.preventDefault();
    setIsDragOver(false);
  };

  const handleDrop = (e: DragEvent) => {
    e.preventDefault();
    setIsDragOver(false);

    addFilesForUpload(e.dataTransfer.files);
  };

  const onUploadProgress = (progressEvent: ProgressEvent) => {
    const percentage = Math.round(
      (100 * progressEvent.loaded) / progressEvent.total
    );
    setProgress(percentage);
  };

  const handleUpload = () => {
    const uploadShelfId = Number(shelfId);
    const uploadFolderId = Number(folderId) || 0;

    const formData = new FormData();

    filesForUpload.forEach((file: File) => {
      formData.append(file.name, file);
    });

    const options = {
      headers: {
        'Shelf-Header': 'File-request',
      },
      signal: abortController.signal,
      onUploadProgress,
    };

    fileServices
      .uploadFiles(uploadShelfId, uploadFolderId, formData, options)
      .then((res) => {
        toast.success(res.data?.message);
        onGetData();
        onCloseModal();
      })
      .catch((err) => {
        setProgress(0);
        toast.error(err.response?.data?.message);
        onCloseModal();
      });
  };

  const handleCloseModal = () => {
    onCloseModal();
  };

  const cancelUploadRequest = () => {
    abortController.abort();
  };

  useEffect(
    () => () => {
      cancelUploadRequest();
    },
    []
  );

  const areFilesForUploadEmpty = filesForUpload.length === 0;

  const ItemWithHandler = ({ file }: { file: File }) => {
    const handleClick = () => {
      const newFiles = filesForUpload.filter((f) => f.name !== file.name);
      setFilesForUpload(newFiles);
    };

    return (
      <AddedFilesListItem>
        {file.name}
        <AddedFilesIconButton onClick={handleClick}>
          <FaTimes />
        </AddedFilesIconButton>
      </AddedFilesListItem>
    );
  };

  return (
    <>
      <DropZoneWrapper
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
        isDragOver={isDragOver}
      >
        {areFilesForUploadEmpty ? (
          <>
            <FaFolderOpen size={theme.size.lg} />
            <AddFilesText>Drag and Drop</AddFilesText>
            <AddFilesText>OR</AddFilesText>
          </>
        ) : (
          <>
            <AddFilesText>Added files</AddFilesText>
            <AddedFilesList>
              {filesForUpload.map((file: File) => (
                <ItemWithHandler key={file.name} file={file} />
              ))}
            </AddedFilesList>
          </>
        )}
        <AddFilesLabel>
          {areFilesForUploadEmpty ? 'Add Files' : 'Add More Files'}
          <AddFilesInput type="file" multiple onChange={handleAddFiles} />
        </AddFilesLabel>
      </DropZoneWrapper>
      {progress > 0 && <ProgressBar progress={progress} />}
      <Footer>
        <ModalButtonDivider>
          <Button variant="secondary" onClick={handleCloseModal}>
            Cancel
          </Button>
          <Button
            onClick={handleUpload}
            disabled={!filesForUpload.length || progress > 0}
          >
            Upload
          </Button>
        </ModalButtonDivider>
      </Footer>
    </>
  );
};

export default UploadModal;
