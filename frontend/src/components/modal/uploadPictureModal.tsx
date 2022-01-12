import { ChangeEvent, DragEvent, useState } from 'react';
import { BsCloudUpload } from 'react-icons/bs';
import { RiCloseCircleFill } from 'react-icons/ri';
import userServices from '../../services/userServices';
import { theme } from '../../theme';
import { AlertMessage } from '../../utils/enums/alertMessages';
import AlertPortal from '../alert/alert';
import { ButtonWrapper } from '../profile/profile.styles';
import { Button } from '../UI/button';
import {
  AddFilesInput,
  AddFilesLabel,
  DropZoneWrapperImage,
  ImageWrapper,
  RemoveImageIconButton,
} from './uploadModal/uploadModal.styles';

interface UploadPictureModalProps {
  onCloseModal: () => void;
}

const UploadPictureModal = ({ onCloseModal }: UploadPictureModalProps) => {
  const [image, setImage] = useState<File | null>(null);
  const [error, setError] = useState<string>('');
  const [isDragOver, setIsDragOver] = useState(false);
  const [previewImage, setPreviewImage] = useState(
    './assets/images/profile.jpg'
  );
  const [abortController] = useState<AbortController>(new AbortController());

  const handleAlertClose = () => setError('');
  const handleCloseModal = () => onCloseModal();

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { files } = e.target;

    if (files?.length === 0) {
      return; // if no image is selected keep previous image in state
    }

    if (files) {
      const extension = files[0].name.substring(files[0].name.lastIndexOf('.'));
      if (
        extension !== '.jpg' &&
        extension !== '.jpeg' &&
        extension !== '.png'
      ) {
        setError('You must select an image');
        return;
      }
      setImage(files[0]);
      setPreviewImage(URL.createObjectURL(files[0]));
    }
  };

  const handleDragOver = (e: DragEvent<HTMLDivElement>): void => {
    e.preventDefault();
    setIsDragOver(true);
  };
  const handleDragLeave = (e: DragEvent<HTMLDivElement>): void => {
    e.preventDefault();
    setIsDragOver(true);
  };
  const handleDrop = (e: DragEvent<HTMLDivElement>): void => {
    e.preventDefault();
    setIsDragOver(false);
    setImage(e.dataTransfer.files[0]);
    setPreviewImage(URL.createObjectURL(e.dataTransfer.files[0]));
  };
  const handleRemoveImage = () => {
    setImage(null);
  };

  const handleUpload = () => {
    if (!image) {
      setError('You must select an image');
      return;
    }
    const formData = new FormData();
    const options = {
      signal: abortController.signal,
    };

    if (image) {
      formData.append(image.name, image);
    }

    userServices
      .uploadProfilePicture(formData, options)
      .then(() => console.log('ok'))
      .catch(() => console.log('nope'));
  };

  return (
    <>
      {error && (
        <AlertPortal
          type={AlertMessage.ERRROR}
          title="Error"
          message={error}
          onClose={handleAlertClose}
        />
      )}
      <DropZoneWrapperImage
        isDragOver={isDragOver}
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
      >
        {image ? (
          <ImageWrapper>
            <RemoveImageIconButton onClick={handleRemoveImage}>
              <RiCloseCircleFill color={theme.colors.secondary} size="23px" />
            </RemoveImageIconButton>
            <img
              src={previewImage}
              alt="profileImage.jpg"
              id="qa-profileImage"
              style={{ height: '200px' }}
            />
          </ImageWrapper>
        ) : (
          <ImageWrapper>
            <BsCloudUpload size={theme.space.xl} />
            Drag and Drop <br /> or <br />
            <AddFilesLabel>
              Browse
              <AddFilesInput type="file" onChange={handleFileChange} />
            </AddFilesLabel>
          </ImageWrapper>
        )}
      </DropZoneWrapperImage>

      <ButtonWrapper>
        <Button onClick={handleCloseModal} type="button" variant="secondary">
          Cancel
        </Button>
        <Button onClick={handleUpload} type="button">
          Upload
        </Button>
      </ButtonWrapper>
    </>
  );
};

export default UploadPictureModal;
