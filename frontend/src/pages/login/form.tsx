import { useMsal } from '@azure/msal-react';
import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, useLocation } from 'react-router-dom';
import { toast } from 'react-toastify';
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
  const { login, microsoftLogin } = useAuth();
  const { instance: msInstance } = useMsal();

  const isLoading = useAppSelector((state: RootState) => state.loading.loading);

  const navigation = useNavigate();
  const location = useLocation();
  const submitForm = (data: LoginData) => {
    login(
      data,
      () => {
        navigation(Routes.DASHBOARD);
      },
      (err) => {
        toast.error(err);
      }
    );
  };

  useEffect(() => {
    if (location.state) {
      toast.info('Go to your email');
    }
  }, []);

  const handleMicrosoftSignIn = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    msInstance
      .acquireTokenPopup(loginRequest)
      .then(({ accessToken }) => {
        microsoftLogin(
          { bearerToken: accessToken },
          () => {
            navigation(Routes.DASHBOARD);
          },
          (err: string) => {
            toast.error(err);
          }
        );
      })
      .catch((err) => {
        if (err.errorCode === 'user_cancelled') return;
        toast.error(err.response?.data?.message);
      });
  };

  return (
    <FormContainer>
      <H2>Log in</H2>
      <Base onSubmit={handleSubmit(submitForm)}>
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
