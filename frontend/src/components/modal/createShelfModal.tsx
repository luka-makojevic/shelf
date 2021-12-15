import { useForm } from 'react-hook-form';
import { CreateShelfData } from '../../interfaces/dataTypes';
import { Base, InputFieldWrapper } from '../form/form-styles';
import { InputField } from '../UI/input/InputField';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Button } from '../UI/button';
import { CreateShelfModalProps } from './modal.interfaces';
import shelfServices from '../../services/shelfServices';
import { useShelf } from '../../hooks/shelfHooks';

export const ROOT_FOLDER = { name: 'Root', id: null, path: [] };

const CreateShelfModal = ({ onCloseModal, onError }: CreateShelfModalProps) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateShelfData>({});

  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const { getShelves } = useShelf();

  const onSubmit = (data: CreateShelfData) => {
    const shelfName = data.name;

    shelfServices
      .createShelf(shelfName)
      .then(() => {
        getShelves(
          {},
          () => {},
          () => {}
        );
      })
      .catch((err) => {
        if (err.response.status === 500) {
          onError('Internal server error');
          return;
        }
        onError(err.response?.data?.message);
      });

    onCloseModal(false);
  };

  const validations = {
    required: 'This field is required',
    maxLength: {
      value: 50,
      message: 'Shelf name can not be longer than 50 characters',
    },
  };

  return (
    <>
      <Base onSubmit={handleSubmit(onSubmit)}>
        <InputFieldWrapper>
          <InputField
            placeholder="Untitled Shelf"
            error={errors.name}
            {...register('name', validations)}
          />
        </InputFieldWrapper>

        <ModalButtonDivider>
          <Button variant="lightBordered" onClick={handleCloseModal}>
            Cancel
          </Button>
          <Button>Create</Button>
        </ModalButtonDivider>
      </Base>
    </>
  );
};

export default CreateShelfModal;
