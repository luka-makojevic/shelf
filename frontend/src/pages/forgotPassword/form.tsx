import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import {
  Base,
  FormContainer,
  InputFieldWrapper,
} from '../../components/form/form-styles';
import { InputField } from '../../components/UI/input/InputField';
import { ForgotPasswordData } from '../../interfaces/dataTypes';
import { forgotPasswordFieldConfig } from '../../utils/validation/config/forgotPasswordValidationConfig';
import userServices from '../../services/userServices';
import { Button } from '../../components/UI/button';
import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { H2 } from '../../components/text/text-styles';
import { Routes } from '../../utils/enums/routes';

const ForgotPasswordForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ForgotPasswordData>();

  const validation = forgotPasswordFieldConfig;
  const navigate = useNavigate();

  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>('');

  const submitData = (data: ForgotPasswordData) => {
    setIsLoading(true);
    userServices
      .forgotPassword(data)
      .then((res) => {
        if (res.data) {
          navigate(Routes.LOGIN, {
            state: 'Go to your email to reset password',
          });
        }
      })
      .catch((err) => {
        setError(err.response?.data?.message);
      })
      .finally(() => setIsLoading(false));
  };

  const handleAlertClose = () => {
    setError('');
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
