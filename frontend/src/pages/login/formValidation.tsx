import { useContext, useState } from 'react';
import { useForm, RegisterOptions } from 'react-hook-form';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField, InputFieldType } from '../../components/input/InputField';
import { Error } from '../../components/text/text-styles';

import { LoginData } from '../../interfaces/types';
import { AuthContext } from '../../providers/authProvider';

interface FieldConfig {
  type: InputFieldType;
  placeholder: string;
  name: 'email' | 'password';

  validations: RegisterOptions;
}

const FormValidation = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginData>({});
  const [error, setError] = useState<string>();

  const fieldConfigs: FieldConfig[] = [
    {
      type: 'email',
      placeholder: 'Email',
      name: 'email',
      validations: {
        required: 'This field is required',
        pattern: {
          value:
            /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i,
          message: 'Invalid email format',
        },
      },
    },
    {
      type: 'password',
      placeholder: 'Password',
      name: 'password',
      validations: {
        required: 'This field is required',
        minLength: {
          value: 8,
          message: 'Password must have at least 8 characters',
        },
        pattern: {
          value:
            /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&.()–[{}\]:;',?/*~$^+=<>])([^\s]){8,}$/i,
          message: 'Invalid password format',
        },
      },
    },
  ];
  const { login, isLoading } = useContext(AuthContext);

  const submitForm = (data: LoginData) => {
    login(
      data,
      (navigation) => {
        navigation('/dashboard');
      },
      (err: string) => {
        setError(err);
      }
    );
  };

  return (
    <Form.Base onSubmit={handleSubmit(submitForm)}>
      <Error>{error}</Error>
      <InputFieldWrapper>
        {fieldConfigs.map((fieldConfig: FieldConfig) => (
          <InputField
            key={fieldConfig.name}
            placeholder={fieldConfig.placeholder}
            error={errors[fieldConfig.name]}
            type={fieldConfig.type}
            {...register(fieldConfig.name, fieldConfig.validations)}
          />
        ))}
      </InputFieldWrapper>

      <Form.Submit isLoading={isLoading}>Sign in</Form.Submit>
    </Form.Base>
  );
};

export default FormValidation;
