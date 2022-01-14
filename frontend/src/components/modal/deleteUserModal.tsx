import { toast } from 'react-toastify';
import { TableDataTypes } from '../../interfaces/dataTypes';
import adminServices from '../../services/adminServices';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Description } from '../text/text-styles';
import { Button } from '../UI/button';
import { DeleteModalBody } from './modal.styles';

const DeleteUserModal = ({
  onCloseModal,
  onDeleteUser,
  user,
}: {
  onCloseModal: () => void;
  onDeleteUser: () => void;
  user: TableDataTypes | null;
}) => {
  const handleCloseModal = () => {
    onCloseModal();
  };

  const handleDelete = () => {
    if (!user) return;
    adminServices
      .deleteUser(user?.id)
      .then(() => {
        onDeleteUser();
        onCloseModal();
      })
      .catch((err) => toast.error(err?.response?.data?.message));
  };

  return (
    <DeleteModalBody>
      <Description>
        {`Are you sure you want to delete user '${user?.name}'? This action is permanent!`}
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

export default DeleteUserModal;
