import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Base, InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import { ForgotPasswordData } from '../../utils/interfaces/dataTypes';
import { Error, PlainText } from '../../components/text/text-styles';
import { forgotPasswordFieldConfig } from '../../utils/validation/config/forgotPasswordValidationConfig';
import userServices from '../../services/userServices';
import { Button } from '../../components/UI/button';

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
    <Base onSubmit={handleSubmit(submitData)}>
      <Error>{error}</Error>
      <PlainText>{successMessage}</PlainText>

      <InputFieldWrapper>
        <InputField
          placeholder="Enter your email"
          error={errors.email}
          type="text"
          {...register('email', validation.validations)}
        />
      </InputFieldWrapper>
      <Button spinner fullwidth isLoading={isLoading}>
        Send
      </Button>
    </Base>
  );
};

export default FormValidation;
