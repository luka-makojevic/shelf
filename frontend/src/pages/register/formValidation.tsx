import React, { useState } from 'react'
import { useForm } from 'react-hook-form'
import { flexDirection } from 'styled-system'
import { Form, InputField, PasswordField } from '../../components/form'
import { InputFieldWrapper } from '../../components/form/form-styles'
import CheckBox from './checkBox'

import { FormData } from './interfaces'

const FormValidation = ({ loading, onSubmit }: any) => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<FormData>({})

  const [passwordVisible, setPasswordVisible] = useState<boolean>(false)

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

  return (
    <>
      <Form.Base onSubmit={handleSubmit(onSubmit)}>
        <InputFieldWrapper>
          {validationRules.map((validationRule) =>
            validationRule.type === 'text' ? (
              <InputField
                key={validationRule.name}
                name={validationRule.name}
                register={register}
                validation={validationRule}
                placeholder={validationRule.placeholder}
                errors={errors}
              />
            ) : (
              <PasswordField
                key={validationRule.name}
                name={validationRule.name}
                register={register}
                validation={validationRule}
                placeholder={validationRule.placeholder}
                errors={errors}
                passwordVisible={passwordVisible}
                onChange={setPasswordVisible}
              />
            )
          )}
          <CheckBox register={register} error={errors.terms?.message} />
        </InputFieldWrapper>

        <Form.Submit loading={loading}>Sign up</Form.Submit>
      </Form.Base>
    </>
  )
}

export default FormValidation
