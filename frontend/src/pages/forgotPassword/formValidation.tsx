import { useContext, useState } from 'react';
import { useForm } from 'react-hook-form';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import { ResetPasswordData } from '../../interfaces/types';
import { AuthContext } from '../../providers/authProvider';
import { Error } from '../../components/text/text-styles';

const EmailFormValidation = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ResetPasswordData>();

  const validation = {
    required: 'This field is required',
    pattern: {
      value:
        /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i,
      message: 'Invalid email format',
    },
  };

  const { resetPass, isLoading } = useContext(AuthContext);
  const [error, setError] = useState<string>('');
  const [successMessage, setSuccessMessage] = useState<string>('');

  const submitData = (data: ResetPasswordData) => {
    // console.log(data);
    resetPass(
      data,
      (res: string) => {
        setSuccessMessage(res);
      },
      (err: string) => {
        setError(err);
        // console.log(err);
      }
    );
  };

  return (
    <Form.Base onSubmit={handleSubmit(submitData)}>
      <Error>{error}</Error>
      <InputFieldWrapper>
        <InputField
          placeholder="Enter your email"
          error={errors.email}
          type="email"
          {...register('email', validation)}
        />
      </InputFieldWrapper>

      <Form.Submit isLoading={isLoading}>Sign in</Form.Submit>
    </Form.Base>
  );
};

export default EmailFormValidation;
