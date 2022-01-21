import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import { ShelfFormData } from '../../interfaces/dataTypes';
import { Base, InputFieldWrapper } from '../form/form-styles';
import { InputField } from '../UI/input/InputField';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Button } from '../UI/button';
import { FunctionEditModalProps } from './modal.interfaces';
import functionService from '../../services/functionService';

const FunctionEditModal = ({
  onCloseModal,
  functionData,
  onEdit,
  onGetData,
}: FunctionEditModalProps) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ShelfFormData>({ defaultValues: { name: functionData?.name } });

  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const onSubmit = (data: ShelfFormData) => {
    const functionName = data.name;
    if (functionData) {
      const payload = {
        functionId: functionData.id,
        newName: functionName,
      };
      functionService
        .renameFunction(payload)
        .then((res) => {
          toast.success(res.data.message);
          onEdit(functionData, functionName);
        })
        .catch((err) => {
          toast.error(err.response?.data.message);
        });
    }

    onCloseModal(false);
  };

  const validations = {
    required: 'This field is required',
    maxLength: {
      value: 50,
      message: 'Function name can not be longer than 50 characters',
    },
  };

  return (
    <>
      <Base onSubmit={handleSubmit(onSubmit)}>
        <InputFieldWrapper>
          <InputField
            placeholder="Untitled function"
            error={errors.name}
            {...register('name', validations)}
          />
        </InputFieldWrapper>

        <ModalButtonDivider>
          <Button variant="lightBordered" onClick={handleCloseModal}>
            Cancel
          </Button>
          <Button>Update</Button>
        </ModalButtonDivider>
      </Base>
    </>
  );
};

export default FunctionEditModal;
