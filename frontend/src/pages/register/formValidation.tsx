import { useContext, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMsal } from '@azure/msal-react';
import { Base, InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import {
  RegisterData,
  RegisterFormData,
  RegisterFieldConfig,
} from '../../utils/interfaces/dataTypes';
import { AuthContext } from '../../providers/authProvider';
import { Error, Link, PlainText } from '../../components/text/text-styles';
import CheckBox from '../../components/checkbox/checkBox';
import { loginRequest } from '../../azure/authConfig';
import { Button } from '../../components/UI/button';
import AlertPortal from '../../components/alert/alert';
import { AlertMessage } from '../../utils/enums/alertMessages';
import { Routes } from '../../utils/enums/routes';
import { config } from '../../utils/validation/config/registerValidationConfig';

const FormValidation = () => {
  const {
    register,
    watch,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>();
  const [error, setError] = useState<string>('');
  const [success, setSuccess] = useState<string>('');

  const {
    register: httpRegister,
    microsoftRegister,
    isLoading,
  } = useContext(AuthContext);
  const { instance } = useMsal();

  const submitForm = ({
    password,
    email,
    firstName,
    lastName,
  }: RegisterData) => {
    httpRegister(
      { password, email, firstName, lastName },
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
  const registeFieldConfig: RegisterFieldConfig[] = config(watch);

  const handleMicrosoftSignUp = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    instance
      .acquireTokenPopup(loginRequest)
      .then(({ accessToken }) => {
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
      })
      .catch((err) => {
        if (err.errorCode === 'user_cancelled') return;
        setError(err.message);
      });
  };

  const handleAlertClose = () => {
    setError('');
    setSuccess('');
  };

  return (
    <Base onSubmit={handleSubmit(submitForm)}>
      {error && (
        <AlertPortal
          type={AlertMessage.ERRROR}
          title="Error"
          message={error}
          onClose={handleAlertClose}
        />
      )}
      {success && (
        <AlertPortal
          type={AlertMessage.INFO}
          title="Info"
          message={success}
          onClose={handleAlertClose}
        />
      )}
      <InputFieldWrapper>
        {registeFieldConfig.map((fieldConfig: RegisterFieldConfig) => (
          <InputField
            key={fieldConfig.name}
            placeholder={fieldConfig.placeholder}
            error={errors[fieldConfig.name]}
            type={fieldConfig.type}
            {...register(fieldConfig.name, fieldConfig.validations)}
          />
        ))}
        <InputFieldWrapper>
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
        </InputFieldWrapper>
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
    </Base>
  );
};
export default FormValidation;
