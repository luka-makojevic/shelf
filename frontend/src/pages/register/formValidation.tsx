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
import {Button} from "../../components/UI/button"
import { Holder } from '../../components/layout/layout.styles';

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

  return (
    <Form.Base onSubmit={handleSubmit(submitForm)}>
      <Holder m="0 auto" maxWidth="200px" minHeight="15px">
        <Error>{error}</Error>
      </Holder>
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
        <Holder flexDirection="column" height="36px">
          <CheckBox
            id="id"
            {...register('areTermsRead', {
              required: 'This field is required',
            })}
          >
            <PlainText>
              I accept{` `}
              <Link to={Routes.TERMS_AND_CONDITIONS} target="_blank">
                Terms of Service
              </Link>
            </PlainText>
          </CheckBox>

          <Error>{errors.areTermsRead?.message}</Error>
        </Holder>
      </InputFieldWrapper>

      <Button
        spinner
        isLoading={isLoading}
        variant="primary"
        fullwidth
        size="large"
      >
        Sign up
      </Button>
    </Form.Base>
  );
};

export default FormValidation;
