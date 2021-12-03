import { useContext, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMsal } from '@azure/msal-react';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import {
  RegisterData,
  RegisterFormData,
  RegisterFieldConfig,
} from '../../interfaces/types';
import { AuthContext } from '../../providers/authProvider';
import { Error, Link, PlainText } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import CheckBox from '../../components/checkbox/checkBox';
import { loginRequest } from '../../azure/authConfig';
import { Button } from '../../components/UI/button';
import { Holder } from '../../components/layout/layout.styles';
import { config } from '../../validation/config/registerValidationConfig';
import AlertContainer from '../../components/alert/alert';

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
    <>
      {error && (
        <AlertContainer
          type="error"
          title="Error"
          message={error}
          onClose={handleAlertClose}
        />
      )}
      {success && (
        <AlertContainer
          type="info"
          title="Info"
          message={success}
          onClose={handleAlertClose}
        />
      )}
      <Form.Base onSubmit={handleSubmit(submitForm)}>
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
    </>
  );
};
export default FormValidation;
