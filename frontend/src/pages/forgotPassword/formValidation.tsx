import { useContext, useState } from 'react';
import { useForm } from 'react-hook-form';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import { ResetPasswordData } from '../../interfaces/types';
import { AuthContext } from '../../providers/authProvider';
import { Error } from '../../components/text/text-styles';
import { emailFormValidation } from '../../validation/config/EmailFormValidation';

const EmailFormValidation = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ResetPasswordData>();

  const validation = emailFormValidation;

  const { resetPassword, isLoading } = useContext(AuthContext);
  const [error, setError] = useState<string>('');
  const [successMessage, setSuccessMessage] = useState<string>('');

  const submitData = (data: ResetPasswordData) => {
    resetPassword(
      data,
      (success: string) => {
        setSuccessMessage(success);
      },
      (err: string) => {
        setError(err);
      }
    );
  };

  return (
    <Form.Base onSubmit={handleSubmit(submitData)}>
      <Error>{error}</Error>
      <p>{successMessage}</p>
      <InputFieldWrapper>
        <InputField
          placeholder="Enter your email"
          error={errors.email}
          type="email"
          {...register('email', { ...validation })}
        />
      </InputFieldWrapper>

      <Form.Submit isLoading={isLoading}>Sign in</Form.Submit>
    </Form.Base>
  );
};

export default EmailFormValidation;
