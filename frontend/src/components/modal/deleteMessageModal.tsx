import { ModalButtonDivider } from '../layout/layout.styles';
import { Description } from '../text/text-styles';
import { Button } from '../UI/button';
import { DeleteShelfModalProps } from './modal.interfaces';
import shelfServices from '../../services/shelfServices';
import { useShelf } from '../../hooks/shelfHooks';
import { DeleteModalBody } from './modal.styles';
import fileServices from '../../services/fileServices';

const DeleteShelfModal = ({
  onCloseModal,
  onError,
  onDelete,
  shelf,
  message,
  selectedData,
}: DeleteShelfModalProps) => {
  const handleCloseModal = () => {
    onCloseModal();
  };

  const { getShelves } = useShelf();

  const handleDelete = () => {
    if (shelf) {
      shelfServices
        .hardDeleteShelf(shelf.id)
        .then(() => {
          getShelves(
            () => {},
            () => {}
          );
        })
        .catch((err) => {
          if (err.response?.status === 500) {
            onError('Internal server error');
            return;
          }
          onError(err.response?.data?.message);
        });
    }

    if (selectedData) {
      const fileIds: number[] = [];
      const folderIds: number[] = [];

      selectedData.forEach((item) => {
        if (item.folder) folderIds.push(item.id);
        else fileIds.push(item.id);
      });

      if (fileIds.length > 0) {
        fileServices
          .hardDeleteFile(fileIds)
          .then(() => {
            onDelete(selectedData);
          })
          .catch((err) => {
            if (err.response?.status === 500) {
              onError('Internal server error');
              return;
            }
            onError(err.response?.data?.message);
          });
      }

      if (folderIds.length > 0) {
        fileServices
          .hardDeleteFolder(folderIds)
          .then(() => onDelete(selectedData))
          .catch((err) => {
            if (err.response?.status === 500) {
              onError('Internal server error');
              return;
            }
            onError(err.response?.data?.message);
          });
      }
    }

    onCloseModal();
  };

  return (
    <DeleteModalBody>
      <Description>
        {shelf &&
          `Are you sure you want to delete '${shelf?.name}' shelf? This action will permanently delete all data inside this shelf!!!`}
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

export default DeleteShelfModal;
