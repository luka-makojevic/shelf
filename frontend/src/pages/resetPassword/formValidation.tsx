import { useParams, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { InputField } from '../../components/input/InputField';
import { Error, PlainText } from '../../components/text/text-styles';
import { Base, InputFieldWrapper } from '../../components/form/form-styles';
import { Button } from '../../components/UI/button';
import {
  PasswordData,
  ResetPasswordFieldConfig,
} from '../../utils/interfaces/dataTypes';
import userServices from '../../services/userServices';

import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { Routes } from '../../utils/enums/routes';
import { config } from '../../utils/validation/config/resetPasswordValidationConfig';

const ResetPasswordForm = () => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>('');
  const [successMessage, setSuccessMessage] = useState<string>('');

  const { jwtToken } = useParams();

  const registerFieldConfig: ResetPasswordFieldConfig[] = config(watch);
  const navigation = useNavigate();

  const onSubmit = ({ password }: PasswordData) => {
    setIsLoading(true);

    const data = { password, jwtToken };

    userServices
      .resetPassword(data)
      .then((res) => {
        setSuccessMessage(res.data.message);
        navigation(Routes.LOGIN);
      })
      .catch((err) => {
        setError(err.response?.data.message);
      })
      .finally(() => setIsLoading(false));
  };

  const handleAlertClose = () => {
    setError('');
  };

  return (
    <>
      {error && (
        <AlertPortal
          type={AlertMessage.ERRROR}
          title="Error"
          message={error}
          onClose={handleAlertClose}
        />
      )}
      <Base onSubmit={handleSubmit(onSubmit)}>
        <Error>{error}</Error>
        <PlainText color="green">{successMessage}</PlainText>
        <InputFieldWrapper>
          {registerFieldConfig.map((fieldConfig) => (
            <InputField
              key={fieldConfig.name}
              placeholder={fieldConfig.placeholder}
              type={fieldConfig.type}
              error={errors[fieldConfig.name]}
              {...register(fieldConfig.name, fieldConfig.validations)}
            />
          ))}
        </InputFieldWrapper>
        <Button spinner fullwidth isLoading={isLoading}>
          Reset Password
        </Button>
      </Base>
    </>
  );
};

export default ResetPasswordForm;
