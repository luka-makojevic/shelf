import { useContext, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link } from 'react-router-dom';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
import {
  RegisterData,
  RegisterFormData,
  RegisterFieldConfig,
  RegisterValidationProps,
} from '../../interfaces/types';
import { AuthContext } from '../../providers/authProvider';
import { Error, PlainText } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import CheckBox from '../../components/checkbox/checkBox';
import { config } from '../../validation/config/registerValidationConfig';

const FormValidation = ({ registerTest }: RegisterValidationProps) => {
  const {
    register,
    watch,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>();
  const [error, setError] = useState<string>();

  const { register: httpRegister, isLoading } = useContext(AuthContext);

  const submitForm = async (data: RegisterData) => {
    httpRegister(
      data,
      () => {},
      (err) => {
        setError(err);
      }
    );
    if (registerTest) {
      try {
        await registerTest(
          data.email,
          data.password,
          data.confirmPassword,
          data.areTermsRead
        );
      } catch (e) {
        console.log(e);
      }
    }

    reset();
  };
  const registeFieldConfig: RegisterFieldConfig[] = config(watch);

  return (
    <Form.Base onSubmit={handleSubmit(submitForm)}>
      <Error>{error}</Error>
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
        <CheckBox
          id="id"
          {...register('areTermsRead', { required: 'This field is required' })}
        >
          <PlainText>
            I accept{` `}
            <Link to={Routes.TERMS_AND_CONDITIONS} target="_blank">
              Terms of Service
            </Link>
          </PlainText>
        </CheckBox>

        {errors.areTermsRead && (
          <Error role="alert">{errors.areTermsRead?.message}</Error>
        )}
      </InputFieldWrapper>

      <Form.Submit isLoading={isLoading}>Sign up</Form.Submit>
    </Form.Base>
  );
};
export default FormValidation;
