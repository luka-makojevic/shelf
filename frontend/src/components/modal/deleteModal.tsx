import { toast } from 'react-toastify';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Description } from '../text/text-styles';
import { Button } from '../UI/button';
import { DeleteModalProps } from './modal.interfaces';
import shelfServices from '../../services/shelfServices';
import { DeleteModalBody } from './modal.styles';
import fileServices from '../../services/fileServices';
import folderService from '../../services/folderService';
import functionServices from '../../services/functionService';

const DeleteModal = ({
  onCloseModal,
  onDeleteShelf,
  onDeleteFiles,
  onDeleteFunction,
  shelf,
  message,
  selectedData,
  functionData,
}: DeleteModalProps) => {
  const handleCloseModal = () => {
    onCloseModal();
  };

  const handleDelete = () => {
    if (functionData && onDeleteFunction) {
      functionServices
        .deleteFunction(functionData.id)
        .then(() => {
          toast.success('Function successfully deleted');
          onDeleteFunction(functionData);
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
    }
    if (shelf && onDeleteShelf) {
      shelfServices
        .hardDeleteShelf(shelf.id)
        .then(() => {
          onDeleteShelf(shelf);
        })
        .catch((err) => {
          toast.error(err.response?.data?.message);
        });
    }

    if (selectedData) {
      const fileIds: number[] = [];
      const folderIds: number[] = [];

      selectedData.forEach((item) => {
        if (item.folder) folderIds.push(item.id);
        else fileIds.push(item.id);
      });

      if (fileIds.length > 0 && onDeleteFiles) {
        fileServices
          .hardDeleteFile(fileIds)
          .then(() => {
            onDeleteFiles(selectedData);
          })
          .catch((err) => {
            toast.error(err.response?.data?.message);
          });
      }

      if (folderIds.length > 0 && onDeleteFiles) {
        folderService
          .hardDeleteFolder(folderIds)
          .then(() => onDeleteFiles(selectedData))
          .catch((err) => {
            toast.error(err.response?.data?.message);
          });
      }
    }

    onCloseModal();
  };

  return (
    <DeleteModalBody>
      <Description>
        {shelf &&
          `Are you sure you want to delete '${shelf?.name}' shelf? This action will permanently delete all data inside this shelf!`}
        {functionData &&
          `Are you sure you want to delete '${functionData?.name}'? This action will permanently delete this function!`}
        {message}
      </Description>
      <ModalButtonDivider>
        <Button variant="lightBordered" onClick={handleCloseModal}>
          Cancel
        </Button>
        <Button onClick={handleDelete}>Delete</Button>
      </ModalButtonDivider>
    </DeleteModalBody>
  );
};

export default DeleteModal;
