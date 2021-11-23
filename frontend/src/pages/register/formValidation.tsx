import { useForm, RegisterOptions } from 'react-hook-form';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField, InputFieldType } from '../../components/input/InputField';
import CheckBox from './checkBox';

import { FormData } from './interfaces';

interface FieldConfig {
  type: InputFieldType;
  placeholder: string;
  name:
    | 'terms'
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
  } = useForm<FormData>({});

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

  const submitForm = (/* data: any */) => {
    // console.log(data);
  };

  return (
    <Form.Base onSubmit={handleSubmit(submitForm)}>
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
        <CheckBox register={register} error={errors.terms?.message} />
      </InputFieldWrapper>

      <Form.Submit>Sign up</Form.Submit>
    </Form.Base>
  );
};

export default FormValidation;
