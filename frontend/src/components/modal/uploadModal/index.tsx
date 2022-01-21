import { ChangeEvent, DragEvent, useEffect, useState } from 'react';
import { FaTimes } from 'react-icons/fa';
import { RiFileZipLine } from 'react-icons/ri';
import { AiFillFile, AiOutlineFileImage } from 'react-icons/ai';
import { BsCloudUpload } from 'react-icons/bs';
import { BiText } from 'react-icons/bi';
import { MdVideoLibrary } from 'react-icons/md';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import fileServices from '../../../services/fileServices';
import { theme } from '../../../theme';
import { ModalButtonDivider } from '../../layout/layout.styles';
import ProgressBar from '../../progressBar';
import { Button } from '../../UI/button';
import { UploadModalProps } from './uploadModal.interfaces';
import {
  AddedFilesIconButton,
  AddedFilesList,
  AddedFilesListItem,
  AddedFilesListItemText,
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

  const itemType = (item: File) => {
    const ext = item.name.substring(item.name.lastIndexOf('.'));
    if (ext === '.jpg' || ext === '.jpeg' || ext === '.png') {
      return <AiOutlineFileImage size={theme.size.sm} />;
    }
    if (ext === '.txt' || ext === '.pdf' || ext === '.doc') {
      return <BiText size={theme.size.sm} />;
    }
    if (ext === '.mp3' || ext === '.mp4') {
      return <MdVideoLibrary size={theme.size.sm} />;
    }
    if (ext === '.zip' || ext === '.7z' || ext === '.rar') {
      return <RiFileZipLine size={theme.size.sm} />;
    }
    return <AiFillFile size={theme.size.sm} />;
  };

  const areFilesForUploadEmpty = filesForUpload.length === 0;

  const ItemWithHandler = ({ file }: { file: File }) => {
    const handleClick = () => {
      const newFiles = filesForUpload.filter((f) => f.name !== file.name);
      setFilesForUpload(newFiles);
    };

    return (
      <AddedFilesListItem>
        <AddedFilesListItemText>
          {itemType(file)}
          <p>{file.name}</p>
        </AddedFilesListItemText>
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
            <BsCloudUpload size={theme.space.xl} />
            <AddFilesText>Drag and Drop</AddFilesText>
            <AddFilesText>OR</AddFilesText>
          </>
        ) : (
          <AddedFilesList>
            {filesForUpload.map((file: File) => (
              <ItemWithHandler key={file.name} file={file} />
            ))}
          </AddedFilesList>
        )}

        <AddFilesLabel>
          Browse
          <AddFilesInput type="file" multiple onChange={handleAddFiles} />
        </AddFilesLabel>
      </DropZoneWrapper>

      {progress > 0 && <ProgressBar progress={progress} />}

      <ModalButtonDivider>
        <Button variant="light" onClick={handleCloseModal}>
          Cancel
        </Button>
        <Button
          onClick={handleUpload}
          disabled={!filesForUpload.length || progress > 0}
          variant="primaryBordered"
        >
          Upload
        </Button>
      </ModalButtonDivider>
    </>
  );
};

export default UploadModal;
