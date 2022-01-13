import { ChangeEvent, DragEvent, useState } from 'react';
import { BsCloudUpload } from 'react-icons/bs';
import { RiCloseCircleFill } from 'react-icons/ri';
import { toast } from 'react-toastify';
import fileServices from '../../services/fileServices';
import { theme } from '../../theme';
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
  id: number;
  onUpdatePicture: (url: string) => void;
}

const UploadPictureModal = ({
  onCloseModal,
  id,
  onUpdatePicture,
}: UploadPictureModalProps) => {
  const [image, setImage] = useState<File | null>(null);
  const [isDragOver, setIsDragOver] = useState(false);
  const [previewImage, setPreviewImage] = useState(
    './assets/images/profile.jpg'
  );
  const [abortController] = useState<AbortController>(new AbortController());

  const handleCloseModal = () => onCloseModal();

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { files } = e.target;

    if (files?.length === 0) {
      return;
    }

    if (files) {
      const extension = files[0].name
        .substring(files[0].name.lastIndexOf('.'))
        .toLowerCase();
      if (
        extension !== '.jpg' &&
        extension !== '.jpeg' &&
        extension !== '.png'
      ) {
        toast.error('You must select a file of type .jpg, .jpeg or .png');
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
      toast.error('You must select an image');
      return;
    }
    const formData = new FormData();
    const options = {
      headers: {
        'Shelf-Header': 'File-request',
      },
      signal: abortController.signal,
    };

    if (image) {
      formData.append('image', image);
    }

    fileServices
      .uploadProfilePicture(id, formData, options)
      .then((res) => {
        toast.success(res.data.message);
        onUpdatePicture(URL.createObjectURL(image));
        onCloseModal();
      })
      .catch((err) => {
        toast.error(err.response?.data?.message);
      });
  };

  return (
    <>
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
              style={{ height: '100%', width: '100%', objectFit: 'contain' }}
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
