import Modal from '.';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Description } from '../text/text-styles';
import { Button } from '../UI/button';
import { DeleteModalProps } from './modal.interfaces';
import { DeleteModalBody } from './modal.styles';

const DeleteModal = ({
  onCloseModal,
  onDelete,
  title,
  message,
}: DeleteModalProps) => (
  <Modal onCloseModal={onCloseModal} title={title}>
    <DeleteModalBody>
      <Description>{message}</Description>
      <ModalButtonDivider>
        <Button variant="lightBordered" onClick={onCloseModal}>
          Cancel
        </Button>
        <Button onClick={onDelete}>Delete</Button>
      </ModalButtonDivider>
    </DeleteModalBody>
  </Modal>
);

export default DeleteModal;
