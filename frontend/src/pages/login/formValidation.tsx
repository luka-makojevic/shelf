import { useMsal } from '@azure/msal-react';
import { useContext, useState } from 'react';
import { useForm, RegisterOptions } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../../azure/authConfig';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField, InputFieldType } from '../../components/input/InputField';
import { Error } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import { Button } from '../../components/UI/button';
import { Holder } from '../../components/layout/layout.styles';
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
      },
    },
  ];

  const { login, microsoftLogin, isLoading } = useContext(AuthContext);
  const { instance } = useMsal();

  const navigation = useNavigate();

  const submitForm = (data: LoginData) => {
    login(
      data,
      () => {
        navigation(Routes.DASHBOARD);
      },
      (err: string) => {
        setError(err);
      }
    );
  };

  const handleMicrosoftSignIn = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    instance.acquireTokenPopup(loginRequest).then(({ accessToken }) => {
      microsoftLogin(
        { bearerToken: accessToken },
        () => {
          navigation(Routes.DASHBOARD);
        },
        (err: string) => {
          setError(err);
        }
      );
    });
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
      </InputFieldWrapper>
      <Button spinner isLoading={isLoading} fullwidth size="large">
        Sign in
      </Button>
      <Button
        onClick={handleMicrosoftSignIn}
        icon={<img src="./assets/images/microsoft-logo.png" alt="" />}
        fullwidth
        size="large"
      >
        Sign in with Microsoft
      </Button>
    </Form.Base>
  );
};

export default FormValidation;
