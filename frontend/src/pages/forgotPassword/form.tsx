import { useState } from 'react';
import { useForm } from 'react-hook-form';
import {
  Base,
  FormContainer,
  InputFieldWrapper,
} from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import { ForgotPasswordData } from '../../utils/interfaces/dataTypes';
import { forgotPasswordFieldConfig } from '../../utils/validation/config/forgotPasswordValidationConfig';
import userServices from '../../services/userServices';
import { Button } from '../../components/UI/button';
import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { H2 } from '../../components/text/text-styles';

const ForgotPasswordForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ForgotPasswordData>();

  const validation = forgotPasswordFieldConfig;

  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>('');
  const [success, setSuccess] = useState<string>('');

  const submitData = (data: ForgotPasswordData) => {
    setIsLoading(true);
    userServices
      .forgotPassword(data)
      .then((res) => {
        if (res.data) {
          setSuccess('Go to your email to reset password');
        }
      })
      .catch((err) => {
        setError(err.response?.data?.message);
      })
      .finally(() => setIsLoading(false));
  };

  const handleAlertClose = () => {
    setError('');
    setSuccess('');
  };

  return (
    <FormContainer>
      <H2>Send email</H2>
      <Base onSubmit={handleSubmit(submitData)}>
        {error && (
          <AlertPortal
            type={AlertMessage.ERRROR}
            title="Error"
            message={error}
            onClose={handleAlertClose}
          />
        )}
        {success && (
          <AlertPortal
            type={AlertMessage.INFO}
            title="Info"
            message={success}
            onClose={handleAlertClose}
          />
        )}
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
      </Base>
    </FormContainer>
  );
};

export default ForgotPasswordForm;
