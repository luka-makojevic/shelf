import { toast } from 'react-toastify';
import { useForm } from 'react-hook-form';
import { ShelfDataType } from '../../interfaces/dataTypes';
import { Base, InputFieldWrapper } from '../form/form-styles';
import { InputField } from '../UI/input/InputField';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Button } from '../UI/button';
import { FolderModalProps } from './modal.interfaces';
import fileServices from '../../services/fileServices';
import folderService from '../../services/folderService';

export const ROOT_FOLDER = { name: 'Root', id: null, path: [] };

const FolderModal = ({
  onCloseModal,
  shelfId,
  folderId,
  placeholder,
  buttonText,
  file,
  onGetData,
  onEdit,
}: FolderModalProps) => {
  const defaultName = file?.folder
    ? file.name
    : file?.name.substring(0, file.name.lastIndexOf('.'));

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ShelfDataType>({
    defaultValues: {
      name: defaultName,
    },
  });

  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const convertedShelfId = Number(shelfId);
  const convertedFolderId = Number(folderId);

  const onSubmit = (data: { name: string }) => {
    const newName = data.name;

    if (file) {
      if (!file?.folder) {
        fileServices
          .editFile({ fileId: file.id, fileName: newName })
          .then(() => {
            onEdit(file, newName);
          })
          .catch((err) => {
            toast.error(err.response?.data?.message);
          });
      } else if (file?.folder) {
        folderService
          .editFolder({ folderId: file.id, folderName: newName })
          .then(() => {
            onEdit(file, newName);
          })
          .catch((err) => {
            toast.error(err.response?.data?.message);
          });
      }
    } else if (folderId) {
      folderService
        .createFolder(newName, convertedShelfId, convertedFolderId)
        .then(() => {
          onGetData();
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
    } else if (!folderId) {
      folderService
        .createFolder(newName, convertedShelfId)
        .then(() => onGetData())
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
    }
    onCloseModal(false);
  };

  const validations = {
    required: 'This field is required',
    maxLength: {
      value: 50,
      message: 'File name can not be longer than 50 characters',
    },
  };

  return (
    <>
      <Base onSubmit={handleSubmit(onSubmit)}>
        <InputFieldWrapper>
          <InputField
            placeholder={placeholder}
            error={errors.name}
            {...register('name', validations)}
          />
        </InputFieldWrapper>

        <ModalButtonDivider>
          <Button variant="light" onClick={handleCloseModal}>
            Cancel
          </Button>
          <Button>{buttonText}</Button>
        </ModalButtonDivider>
      </Base>
    </>
  );
};

export default FolderModal;
