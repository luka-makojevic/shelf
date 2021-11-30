import { useContext, useState } from 'react';
import { useForm } from 'react-hook-form';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import { Error } from '../../components/text/text-styles';
import { Button } from '../../components/UI/button';
import { Holder } from '../../components/layout/layout.styles';
import { loginFieldConfig } from '../../validation/config/loginValidationConfig';
import {
  LoginData,
  LoginFieldConfig,
  LoginValidationProps,
} from '../../interfaces/types';
import { AuthContext } from '../../providers/authProvider';
import { Routes } from '../../enums/routes';

const FormValidation = ({ login }: LoginValidationProps) => {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<LoginData>({});
  const [error, setError] = useState<string>();

  const { login: HttpLogin, isLoading } = useContext(AuthContext);

  const submitForm = async (data: LoginData) => {
    HttpLogin(
      data,
      (navigation) => {
        navigation(Routes.DASHBOARD);
      },
      (err: string) => {
        setError(err);
      }
    );
    if (login) {
      try {
        await login(data.email, data.password);
        throw 'error';
      } catch (e) {
        console.log(e);
      }
    }

    reset();
  };

  return (
    <Form.Base onSubmit={handleSubmit(submitForm)}>
      <Holder m="0 auto" maxWidth="200px" minHeight="15px">
        <Error>{error}</Error>
      </Holder>
      <InputFieldWrapper>
        {loginFieldConfig.map((fieldConfig: LoginFieldConfig) => (
          <InputField
            key={fieldConfig.name}
            placeholder={fieldConfig.placeholder}
            error={errors[fieldConfig.name]}
            type={fieldConfig.type}
            {...register(fieldConfig.name, fieldConfig.validations)}
          />
        ))}
      </InputFieldWrapper>

      <Button spinner isLoading={isLoading} fullwidth size="large">
        Sign in
      </Button>
    </Form.Base>
  );
};

export default FormValidation;
