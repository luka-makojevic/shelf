import { useForm } from 'react-hook-form'
import Form from '../../components/form'
import { InputFieldWrapper } from '../../components/form/form-styles'
import InputField from '../../components/input/InputField'
import CheckBox from './checkBox'

import { FormData } from './interfaces'

const FormValidation = () => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<FormData>({})

  const validationRules = [
    {
      type: 'text',
      placeholder: 'Email',
      name: 'email',
      required: 'This field is required',
      pattern: {
        value:
          /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i,
        message: 'Invalid email format',
      },
    },
    {
      type: 'password',
      placeholder: 'Password',
      name: 'password',
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
    {
      type: 'password',
      placeholder: 'Confirm Password',
      name: 'confirmPassword',
      required: 'This field is required',
      validate: (value: string) =>
        value === watch('password') || 'Passwords must match',
    },
    {
      type: 'text',
      placeholder: 'First Name',
      name: 'firstName',
      required: 'This field is required',
    },
    {
      type: 'text',
      placeholder: 'Last name',
      name: 'lastName',
      required: 'This field is required',
    },
  ]

  const submitForm = (data: any) => {
    console.log(data)
  }

  return (
    <Form.Base onSubmit={handleSubmit(submitForm)}>
      <InputFieldWrapper>
        {validationRules.map((validationRule) => (
          <InputField
            key={validationRule.name}
            name={validationRule.name}
            placeholder={validationRule.placeholder}
            validationRule={validationRule}
            register={register}
            errors={errors}
            type={validationRule.type}
          />
        ))}
        <CheckBox register={register} error={errors.terms?.message} />
      </InputFieldWrapper>

      <Form.Submit>Sign up</Form.Submit>
    </Form.Base>
  )
}

export default FormValidation
