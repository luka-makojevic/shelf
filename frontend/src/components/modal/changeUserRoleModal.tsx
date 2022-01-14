import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import { TableDataTypes } from '../../interfaces/dataTypes';
import adminServices from '../../services/adminServices';
import { Base } from '../form/form-styles';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Button } from '../UI/button';
import { Select } from '../UI/select';

const options = [
  { value: '1', text: 'Super Admin' },
  { value: '2', text: 'Moderator' },
  { value: '3', text: 'User' },
];

const ChangeUserRoleModal = ({
  onCloseModal,
  onRoleChange,
  user,
}: {
  onCloseModal: () => void;
  onRoleChange: () => void;
  user: TableDataTypes | null;
}) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (data: { role: string }) => {
    if (!user) return;
    const roleId = Number(data.role);
    adminServices
      .changeUserRole(user?.id, roleId)
      .then(() => {
        onRoleChange();
        onCloseModal();
      })
      .catch((err) => toast.error(err?.response?.data?.message));
  };

  const handleCloseModal = () => {
    onCloseModal();
  };

  return (
    <>
      <Base onSubmit={handleSubmit(onSubmit)}>
        <Select
          optionsData={options}
          selectName="role"
          error={errors.role}
          register={register}
          placeHolder="Choose new role"
        />
        <ModalButtonDivider>
          <Button variant="lightBordered" onClick={handleCloseModal}>
            Cancel
          </Button>
          <Button>Change</Button>
        </ModalButtonDivider>
      </Base>
    </>
  );
};

export default ChangeUserRoleModal;
