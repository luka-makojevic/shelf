import { useForm } from 'react-hook-form';
import { ShelfFormData } from '../../interfaces/dataTypes';
import { Base, InputFieldWrapper } from '../form/form-styles';
import { InputField } from '../UI/input/InputField';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Button } from '../UI/button';
import shelfServices from '../../services/shelfServices';
import { ShelfModalProps } from './modal.interfaces';
import { useShelf } from '../../hooks/shelfHooks';

const ShelfModal = ({ onCloseModal, onError, shelf }: ShelfModalProps) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ShelfFormData>({ defaultValues: { name: shelf?.name } });

  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const { getShelves } = useShelf();

  const onSubmit = (data: ShelfFormData) => {
    const shelfName = data.name;

    if (!shelf) {
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
          if (err?.response?.status === 500) {
            onError('Internal server error');
            return;
          }
          onError(err.response?.data?.message);
        });
    } else {
      shelfServices
        .editShelf({ shelfId: shelf.id, shelfName: data.name })
        .then(() => {
          getShelves(
            {},
            () => {},
            () => {}
          );
        })
        .catch((err) => {
          if (err?.response?.status === 500) {
            onError('Internal server error');
            return;
          }
          onError(err.response?.data?.message);
        });
    }

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
            placeholder="Untitled shelf"
            error={errors.name}
            {...register('name', validations)}
          />
        </InputFieldWrapper>

        <ModalButtonDivider>
          <Button variant="lightBordered" onClick={handleCloseModal}>
            Cancel
          </Button>
          <Button>{shelf ? 'Update' : 'Create'}</Button>
        </ModalButtonDivider>
      </Base>
    </>
  );
};

export default ShelfModal;
