import { useParams, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Form } from '../../components';
import { InputField } from '../../components/input/InputField';
import { Error, PlainText } from '../../components/text/text-styles';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { Button } from '../../components/UI/button';
import { PasswordData, ResetPasswordFieldConfig } from '../../interfaces/types';
import userServices from '../../services/userServices';
import { config } from '../../validation/config/resetPasswordValidationConfig';
import { Routes } from '../../enums/routes';
import AlertContainer from '../../components/alert/alert';

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
        <AlertContainer
          type="error"
          title="Error"
          message={error}
          onClose={handleAlertClose}
        />
      )}
      <Form.Base onSubmit={handleSubmit(onSubmit)}>
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
      </Form.Base>
    </>
  );
};

export default ResetPasswordForm;
