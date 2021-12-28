import { toast } from 'react-toastify';
import { useForm } from 'react-hook-form';
import { ShelfDataType } from '../../interfaces/dataTypes';
import { Base, InputFieldWrapper } from '../form/form-styles';
import { InputField } from '../UI/input/InputField';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Button } from '../UI/button';
import { FolderModalProps } from './modal.interfaces';
import { useFiles } from '../../hooks/fileHooks';
import fileServices from '../../services/fileServices';

export const ROOT_FOLDER = { name: 'Root', id: null, path: [] };

const FolderModal = ({
  onCloseModal,
  shelfId,
  folderId,
  placeholder,
  buttonText,
  file,
  getData,
}: FolderModalProps) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ShelfDataType>({ defaultValues: { name: file?.name } });

  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const { getShelfFiles, getFolderFiles } = useFiles();
  const convertedShelfId = Number(shelfId);
  const convertedFolderId = Number(folderId);

  const onSubmit = (data: { name: string }) => {
    const newName = data.name;

    if (file) {
      if (!file?.folder) {
        fileServices
          .editFile({ fileId: file.id, fileName: newName })
          .then(() => {
            getData();
          })
          .catch((err) => {
            toast.error(err.response?.data?.message);
          });
      } else if (file?.folder) {
        fileServices
          .editFolder({ folderId: file.id, folderName: newName })
          .then(() => {
            getData();
          })
          .catch((err) => {
            toast.error(err.response?.data?.message);
          });
      }
    } else if (folderId) {
      fileServices
        .createFolder(newName, convertedShelfId, convertedFolderId)
        .then(() => {
          getFolderFiles(
            convertedFolderId,
            () => {},
            () => {}
          );
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
    } else if (!folderId) {
      fileServices
        .createFolder(newName, convertedShelfId)
        .then(() => {
          getShelfFiles(
            convertedShelfId,
            () => {},
            () => {}
          );
        })
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
