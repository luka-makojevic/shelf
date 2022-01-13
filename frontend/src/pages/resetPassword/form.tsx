import { useParams, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import { InputField } from '../../components/UI/input/InputField';
import { H2 } from '../../components/text/text-styles';
import {
  Base,
  FormContainer,
  InputFieldWrapper,
} from '../../components/form/form-styles';
import { Button } from '../../components/UI/button';
import {
  PasswordData,
  ResetPasswordFieldConfig,
} from '../../interfaces/dataTypes';
import userServices from '../../services/userServices';

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

  const { jwtToken } = useParams();

  const registerFieldConfig: ResetPasswordFieldConfig[] = config(watch);
  const navigation = useNavigate();

  const onSubmit = ({ password }: PasswordData) => {
    setIsLoading(true);

    const data = { password, jwtToken };

    userServices
      .resetPassword(data)
      .then(() => {
        navigation(Routes.LOGIN);
      })
      .catch((err) => {
        toast.error(err.response?.data.message);
      })
      .finally(() => setIsLoading(false));
  };

  return (
    <FormContainer>
      <H2>Reset Password</H2>
      <Base onSubmit={handleSubmit(onSubmit)}>
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
    </FormContainer>
  );
};

export default ResetPasswordForm;
