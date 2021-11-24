// import { useState } from 'react';
import { useForm } from 'react-hook-form';
import Form from '../../components/form';
import { InputFieldWrapper } from '../../components/form/form-styles';
import { InputField } from '../../components/input/InputField';
// import { Error } from '../../components/text/text-styles';

interface EmailFormData {
  email: string;
}

const EmailFormValidation = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<EmailFormData>();

  // const [error, setError] = useState<string>();

  const validation = {
    required: 'This field is required',
    pattern: {
      value:
        /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i,
      message: 'Invalid email format',
    },
  };

  const isLoading = false;

  const submitData = (/* data: EmailFormData */) => {
    // TODO: sending data to BE
    // console.log(data);
  };

  return (
    <Form.Base onSubmit={handleSubmit(submitData)}>
      {/* <Error>{error}</Error> */}
      <InputFieldWrapper>
        <InputField
          placeholder="Enter your email"
          error={errors.email}
          type="email"
          {...register('email', validation)}
        />
      </InputFieldWrapper>

      <Form.Submit isLoading={isLoading}>Sign in</Form.Submit>
    </Form.Base>
  );
};

export default EmailFormValidation;
