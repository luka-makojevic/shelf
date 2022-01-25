import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMsal } from '@azure/msal-react';
import { toast } from 'react-toastify';
import {
  Base,
  FormContainer,
  InputFieldWrapper,
} from '../../components/form/form-styles';
import { InputField } from '../../components/UI/input/InputField';
import {
  RegisterData,
  RegisterFormData,
  RegisterFieldConfig,
} from '../../interfaces/dataTypes';
import { Error, H2, Link, PlainText } from '../../components/text/text-styles';
import CheckBox from '../../components/UI/checkbox/checkBox';
import { loginRequest } from '../../azure/authConfig';
import { Button } from '../../components/UI/button';
import { Routes } from '../../utils/enums/routes';
import { config } from '../../utils/validation/config/registerValidationConfig';
import { useAppSelector } from '../../store/hooks';
import { RootState } from '../../store/store';
import { useAuth } from '../../hooks/authHook';
import PasswordRequirementsTooltip from './passwordRequirementsTooltip';

const RegisterForm = () => {
  const {
    register,
    watch,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>();

  const { register: httpRegister, microsoftRegister } = useAuth();
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
        toast.info('A verification link has been sent to your email address.');

        reset();
      },
      (err) => {
        toast.error(err);
      }
    );
  };
  const registeFieldConfig: RegisterFieldConfig[] = config(watch);

  const isLoading = useAppSelector((state: RootState) => state.loading.loading);

  const handleMicrosoftSignUp = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    instance
      .acquireTokenPopup(loginRequest)
      .then(({ accessToken }) => {
        microsoftRegister(
          { bearerToken: accessToken },
          () => {
            toast.success('Registered Successfully');
          },
          (err) => {
            toast.error(err);
          }
        );
      })
      .catch((err) => {
        if (err.errorCode === 'user_cancelled') return;
        toast.error(err.message);
      });
  };

  const [isPasswordTooltipVisible, setIsPasswordTooltipVisible] =
    useState(false);

  const handleInputFocus = (e: React.FocusEvent<HTMLInputElement>) => {
    if (e.target.name === 'password') {
      setIsPasswordTooltipVisible(true);
    }
  };

  const handleInputBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    if (e.target.name === 'password') {
      setIsPasswordTooltipVisible(false);
    }
  };

  return (
    <FormContainer>
      <H2>Register</H2>

      <Base onSubmit={handleSubmit(submitForm)}>
        <InputFieldWrapper>
          {registeFieldConfig.map((fieldConfig: RegisterFieldConfig) => (
            <InputField
              key={fieldConfig.name}
              placeholder={fieldConfig.placeholder}
              error={errors[fieldConfig.name]}
              type={fieldConfig.type}
              {...register(fieldConfig.name, fieldConfig.validations)}
              onFocus={handleInputFocus}
              onBlur={handleInputBlur}
            />
          ))}
          {isPasswordTooltipVisible && <PasswordRequirementsTooltip />}
          <InputFieldWrapper>
            <CheckBox
              id="termsOfServices"
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
      <PlainText>
        {`Already have an account?  `}
        <Link to={Routes.LOGIN}>Sign in</Link>
      </PlainText>
    </FormContainer>
  );
};
export default RegisterForm;
