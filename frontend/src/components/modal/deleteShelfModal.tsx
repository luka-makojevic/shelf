import { ModalButtonDivider } from '../layout/layout.styles';
import { Description } from '../text/text-styles';
import { Button } from '../UI/button';
import { DeleteShelfModalProps } from './modal.interfaces';
import shelfServices from '../../services/shelfServices';
import { useShelf } from '../../hooks/shelfHooks';
import { DeleteModalBody } from './modal.styles';

const DeleteShelfModal = ({
  onCloseModal,
  onError,
  shelf,
}: DeleteShelfModalProps) => {
  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const { getShelves } = useShelf();

  const handleDeleteShelf = () => {
    if (shelf) {
      shelfServices
        .hardDeleteShelf(shelf.id)
        .then(() => {
          getShelves(
            {},
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

    onCloseModal(false);
  };

  return (
    <DeleteModalBody>
      <Description>
        Are you sure you want to delete &quot;{shelf?.name}&quot; shelf?
      </Description>
      <ModalButtonDivider>
        <Button variant="lightBordered" onClick={handleCloseModal}>
          Cancel
        </Button>
        <Button onClick={handleDeleteShelf}>Delete</Button>
      </ModalButtonDivider>
    </DeleteModalBody>
  );
};

export default DeleteShelfModal;
