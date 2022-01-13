import { toast } from 'react-toastify';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Description } from '../text/text-styles';
import { Button } from '../UI/button';
import { DeleteModalProps } from './modal.interfaces';
import shelfServices from '../../services/shelfServices';
import { DeleteModalBody } from './modal.styles';
import fileServices from '../../services/fileServices';
import folderService from '../../services/folderService';

const DeleteModal = ({
  onCloseModal,
  onDeleteShelf,
  onDeleteFiles,
  shelf,
  message,
  selectedData,
}: DeleteModalProps) => {
  const handleCloseModal = () => {
    onCloseModal();
  };

  const handleDelete = () => {
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
