import { useForm } from 'react-hook-form';
import { CreateShelfData } from '../../utils/interfaces/dataTypes';
import { CreateShelfModalProps } from '../../utils/interfaces/propTypes';
import { Base, InputFieldWrapper } from '../form/form-styles';
import { InputField } from '../input/InputField';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Button } from '../UI/button';

const CreateShelfModal = ({ onCloseModal }: CreateShelfModalProps) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateShelfData>({});

  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const onSubmit = (/* data: CreateShelfData */) => {
    // console.log('submit', data);

    onCloseModal(false);
  };

  const validations = {
    required: 'This field is required',
  };

  return (
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
  );
};

export default CreateShelfModal;
