import { useContext, useState } from 'react';
import { useForm, RegisterOptions } from 'react-hook-form';
import { Link } from 'react-router-dom';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField, InputFieldType } from '../../components/input/InputField';
import { RegisterData, RegisterFormData } from '../../interfaces/types';
import { AuthContext } from '../../providers/authProvider';
import { Error, PlainText } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import CheckBox from '../../components/checkbox/checkBox';

interface FieldConfig {
  type: InputFieldType;
  placeholder: string;
  name:
    | 'areTermsRead'
    | 'email'
    | 'password'
    | 'confirmPassword'
    | 'firstName'
    | 'lastName';
  validations: RegisterOptions;
}

const FormValidation = () => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<RegisterFormData>();
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
    {
      type: 'password',
      placeholder: 'Confirm Password',
      name: 'confirmPassword',
      validations: {
        required: 'This field is required',
        validate: (value: string) =>
          value === watch('password') || 'Passwords must match',
      },
    },
    {
      type: 'text',
      placeholder: 'First Name',
      name: 'firstName',
      validations: {
        required: 'This field is required',
      },
    },
    {
      type: 'text',
      placeholder: 'Last name',
      name: 'lastName',
      validations: {
        required: 'This field is required',
      },
    },
  ];
  const { register: httpRegister, isLoading } = useContext(AuthContext);

  const submitForm = (data: RegisterData) => {
    httpRegister(
      data,
      () => {},
      (err) => {
        setError(err);
      }
    );
  };

  const tos = (
    <PlainText>
      I accept {` `}
      <Link to={Routes.TERMS} target="_blank">
        Terms of Service
      </Link>
    </PlainText>
  );

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
        <CheckBox
          label={tos}
          {...register('areTermsRead', { required: 'This is required' })}
        />

        <Error>{errors.areTermsRead?.message}</Error>
      </InputFieldWrapper>

      <Form.Submit isLoading={isLoading}>Sign up</Form.Submit>
    </Form.Base>
  );
};

export default FormValidation;
