import { ChangeEvent, DragEvent, useEffect, useState } from 'react';
import { FaFolderOpen } from 'react-icons/fa';
import { useParams } from 'react-router-dom';
import { useFiles } from '../../../hooks/fileHooks';
import fileServices from '../../../services/fileServices';
import { theme } from '../../../theme';
import { ModalButtonDivider } from '../../layout/layout.styles';
import ProgressBar from '../../progressBar';
import { Button } from '../../UI/button';
import { Footer } from '../modal.styles';
import { UploadModalProps } from './uploadModal.interfaces';
import {
  AddedFilesText,
  AddFilesInput,
  AddFilesLabel,
  AddFilesText,
  DropZoneWrapper,
} from './uploadModal.styles';

const UploadModal = ({
  onCloseModal,
  onError,
  onSuccess,
}: UploadModalProps) => {
  const [isDragOver, setIsDragOver] = useState(false);
  const [filesForUpload, setFilesForUpload] = useState<File[]>([]);
  const [progress, setProgress] = useState(0);
  const { shelfId, folderId } = useParams();
  const { getShelfFiles, getFolderFiles } = useFiles();

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
        onSuccess(res.data.message);

        if (folderId !== undefined) {
          getFolderFiles(
            uploadFolderId,
            () => {},
            () => {}
          );
        } else {
          getShelfFiles(
            uploadShelfId,
            () => {},
            () => {}
          );
        }

        onCloseModal(false);
      })
      .catch((err) => {
        setProgress(0);
        onError(err.response?.data?.message);
        onCloseModal(false);
      });
  };

  const handleCloseModal = () => {
    onCloseModal(false);
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

  return (
    <>
      <DropZoneWrapper
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
        isDragOver={isDragOver}
      >
        <FaFolderOpen size={theme.size.lg} />
        <AddFilesText>Drag and Drop</AddFilesText>
        <AddFilesText>OR</AddFilesText>
        <AddFilesLabel>
          Add Files
          <AddFilesInput type="file" multiple onChange={handleAddFiles} />
        </AddFilesLabel>
        {filesForUpload.length > 0 && (
          <AddedFilesText>
            Added files:{' '}
            {filesForUpload.map(
              (file: File, i) =>
                `${file.name}${i < filesForUpload.length - 1 ? ', ' : ''}`
            )}
          </AddedFilesText>
        )}
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
