import { useMsal } from '@azure/msal-react';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, useLocation } from 'react-router-dom';
import { loginRequest } from '../../azure/authConfig';
import {
  Base,
  FormContainer,
  InputFieldWrapper,
} from '../../components/form/form-styles';
import { InputField } from '../../components/UI/input/InputField';
import { Routes } from '../../utils/enums/routes';
import { Button } from '../../components/UI/button';
import { loginFieldConfig } from '../../utils/validation/config/loginValidationConfig';
import { LoginData, LoginFieldConfig } from '../../interfaces/dataTypes';
import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { useAppSelector } from '../../store/hooks';
import { RootState } from '../../store/store';
import { useAuth } from '../../hooks/authHook';
import { H2, Link, PlainText } from '../../components/text/text-styles';

const LoginForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginData>({});
  const [error, setError] = useState<string>();
  const { login, microsoftLogin } = useAuth();
  const { instance } = useMsal();

  const isLoading = useAppSelector((state: RootState) => state.loading.loading);

  const navigation = useNavigate();
  const location = useLocation();

  const submitForm = (data: LoginData) => {
    login(
      data,
      () => {
        navigation(Routes.DASHBOARD);
      },
      () => {
        setError('Authentication credentials not valid');
      }
    );
  };

  const handleMicrosoftSignIn = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    instance
      .acquireTokenPopup(loginRequest)
      .then(({ accessToken }) => {
        microsoftLogin(
          { bearerToken: accessToken },
          () => {
            navigation(Routes.DASHBOARD);
          },
          (err: string) => {
            setError(err);
          }
        );
      })
      .catch((err) => {
        if (err.errorCode === 'user_cancelled') return;
        setError('Authentication credentials not valid');
      });
  };

  const handleAlertClose = () => {
    setError('');
    navigation(Routes.LOGIN);
  };

  return (
    <FormContainer>
      <H2>Log in</H2>
      <Base onSubmit={handleSubmit(submitForm)}>
        {error && (
          <AlertPortal
            type={AlertMessage.ERRROR}
            title="Error"
            message={error}
            onClose={handleAlertClose}
          />
        )}
        {location.state && (
          <AlertPortal
            type={AlertMessage.INFO}
            title="Info"
            message={location.state}
            onClose={handleAlertClose}
          />
        )}
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
        <Button
          onClick={handleMicrosoftSignIn}
          icon={<img src="./assets/images/microsoft-logo.png" alt="" />}
          fullwidth
          size="large"
        >
          Sign in with Microsoft
        </Button>
      </Base>
      <PlainText>
        {`New to the shelf?  `}
        <Link to={Routes.REGISTER}>Sign up</Link>
      </PlainText>
      <PlainText>
        {`Forgot password?  `}
        <Link to={Routes.FORGOT_PASSWORD}>Reset password</Link>
      </PlainText>
    </FormContainer>
  );
};

export default LoginForm;
