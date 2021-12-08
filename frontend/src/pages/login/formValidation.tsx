import { useMsal } from '@azure/msal-react';
import { useContext, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../../azure/authConfig';
import { Base, InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import { Routes } from '../../utils/enums/routes';
import { Button } from '../../components/UI/button';
import { loginFieldConfig } from '../../utils/validation/config/loginValidationConfig';
import { LoginData, LoginFieldConfig } from '../../utils/interfaces/dataTypes';
import { AuthContext } from '../../providers/authProvider';
import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';

const FormValidation = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginData>({});
  const [error, setError] = useState<string>();
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
        setError(err.message);
      });
  };

  const handleAlertClose = () => {
    setError('');
  };

  return (
    <>
      {error && (
        <AlertPortal
          type={AlertMessage.ERRROR}
          title="Error"
          message={error}
          onClose={handleAlertClose}
        />
      )}
      {/* {error && <p>{error}</p>} */}
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
    </>
  );
};

export default FormValidation;
