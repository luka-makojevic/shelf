import { useContext, useState } from 'react';
import { useForm, RegisterOptions } from 'react-hook-form';
import { Link } from 'react-router-dom';
import { useMsal } from '@azure/msal-react';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField, InputFieldType } from '../../components/input/InputField';
import { RegisterData, RegisterFormData } from '../../interfaces/types';
import { AuthContext } from '../../providers/authProvider';
import { Error, PlainText, Success } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import CheckBox from '../../components/checkbox/checkBox';
import { loginRequest } from '../../azure/authConfig';
import { Button } from '../../components/UI/button';
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
    reset,
  } = useForm<RegisterFormData>();
  const [error, setError] = useState<string>('');
  const [success, setSuccess] = useState<string>('');

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

  const {
    register: httpRegister,
    microsoftRegister,
    isLoading,
  } = useContext(AuthContext);
  const { instance } = useMsal();

  const submitForm = (data: RegisterData) => {
    httpRegister(
      data,
      () => {
        setSuccess('A verification link has been sent to your email address.');
        setError('');
        reset();
      },
      (err) => {
        setError(err);
        setSuccess('');
      }
    );
  };

  const handleMicrosoftSignUp = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    instance.acquireTokenPopup(loginRequest).then(({ accessToken }) => {
      microsoftRegister(
        { bearerToken: accessToken },
        () => {
          setSuccess('Registered Successfully');
          setError('');
        },
        (err) => {
          setError(err);
          setSuccess('');
        }
      );
    });
  };

  return (
    <Form.Base onSubmit={handleSubmit(submitForm)}>
      <Holder m="0 auto" maxWidth="200px" minHeight="15px">
        {error && <Error>{error}</Error>}
        {success && <Success>{success}</Success>}
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
      <Button
        onClick={handleMicrosoftSignUp}
        icon={<img src="./assets/images/microsoft-logo.png" alt="" />}
        fullwidth
        size="large"
      >
        Sign up with Microsoft
      </Button>
    </Form.Base>
  );
};

export default FormValidation;
