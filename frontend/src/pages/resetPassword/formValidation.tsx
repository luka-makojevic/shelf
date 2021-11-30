import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { NavigateFunction, useParams } from 'react-router';
import { Form } from '../../components';
import { InputField } from '../../components/input/InputField';
import { Error, PlainText } from '../../components/text/text-styles';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { Button } from '../../components/UI/button';
// import { Routes } from '../../enums/routes';
import {
  ResetPasswordData,
  ResetPasswordFieldConfig,
} from '../../interfaces/types';
import userServices from '../../services/userServices';
import { config } from '../../validation/config/resetPasswordValidationConfig';

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

  const { token } = useParams();

  const registerFieldConfig: ResetPasswordFieldConfig[] = config(watch);

  // useEffect(() => {
  //   console.log({ token: token });  -- will be deleted, just need it to test this
  // }, []);

  const onSubmit = ({ password }: ResetPasswordData) => {
    // console.log(password); -- will be deleted, just need it to test this
    setIsLoading(true);

    userServices
      .resetPassword({ password }, token)
      .then((res) => {
        // console.log({ password, token }); -- will be deleted, just need it to test this
        setSuccessMessage(res.data.message);
        // console.log(res.data.message); -- will be deleted, just need it to test this
      })
      .catch((err) => {
        setError(err.response?.data.message);
        // console.log({ password, token }); -- will be deleted, just need it to test this
      })
      .finally(() => setIsLoading(false));
  };

  return (
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
  );
};

export default ResetPasswordForm;
