import { useForm } from 'react-hook-form';
import { Form } from '..';
import { InputFieldWrapper } from '../form/form-styles';
import { InputField } from '../input/InputField';
import { Button } from '../UI/button';
import { CreateShelfData, CreateShelfModalProps } from '../../interfaces/types';

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
    <Form.Base onSubmit={handleSubmit(onSubmit)}>
      <InputFieldWrapper>
        <InputField
          placeholder="Untitled Shelf"
          error={errors.name}
          {...register('name', validations)}
        />
      </InputFieldWrapper>
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          width: '100%',
          marginTop: '20px',
        }}
      >
        <Button variant="lightBordered" onClick={handleCloseModal}>
          Cancel
        </Button>
        <Button>Create</Button>
      </div>
    </Form.Base>
  );
};

export default CreateShelfModal;
