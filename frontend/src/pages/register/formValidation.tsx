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
import { Button } from '../../components/UI/button';
import { Holder } from '../../components/layout/layout.styles';
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

  const submitForm = (data: RegisterData) => {
    httpRegister(
      data,
      () => {},
      (err) => {
        setError(err);
      }
    );
    reset();
  };
  const registeFieldConfig: RegisterFieldConfig[] = config(watch);

  return (
    <Form.Base onSubmit={handleSubmit(submitForm)}>
      <Holder m="0 auto" maxWidth="200px" minHeight="15px">
        <Error>{error}</Error>
      </Holder>
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
    </Form.Base>
  );
};
export default FormValidation;
