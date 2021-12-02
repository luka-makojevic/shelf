import { useState } from 'react';
import { useForm } from 'react-hook-form';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import { ForgotPasswordData } from '../../interfaces/types';
import { Error, PlainText } from '../../components/text/text-styles';
import { forgotPasswordFieldConfig } from '../../validation/config/forgotPasswordValidationConfig';
import userServices from '../../services/userServices';
import { Button } from '../../components/UI/button';
import { Holder } from '../../components/layout/layout.styles';

const FormValidation = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ForgotPasswordData>();

  const validation = forgotPasswordFieldConfig;

  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>('');
  const [successMessage, setSuccessMessage] = useState<string>('');

  const submitData = (data: ForgotPasswordData) => {
    setIsLoading(true);
    userServices
      .forgotPassword(data)
      .then((res) => {
        if (res.data) {
          setSuccessMessage('Go to your email to reset password');
        }
      })
      .catch((err) => {
        setError(err.response?.data?.message);
      })
      .finally(() => setIsLoading(false));
  };

  return (
    <Form.Base onSubmit={handleSubmit(submitData)}>
      <Holder m="0 auto" maxWidth="300px" minHeight="15px">
        <Error>{error}</Error>
        <PlainText color="green">{successMessage}</PlainText>
      </Holder>

      <InputFieldWrapper>
        <InputField
          placeholder="Enter your email"
          error={errors.email}
          type="email"
          {...register('email', validation.validations)}
        />
      </InputFieldWrapper>

      <Button spinner fullwidth isLoading={isLoading}>
        Send
      </Button>
    </Form.Base>
  );
};

export default FormValidation;
