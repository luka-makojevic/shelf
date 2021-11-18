import React, { useState, useRef } from 'react'
import { useForm, FormProvider, useFormContext } from 'react-hook-form'
import { Form } from '../../components'
import {
  Error,
  Input,
  InputPassword,
  PasswordContainer,
  SeenIcon,
} from '../../components/form/form-styles'
import { Title } from '../../components/text/text-styles'

type Props = {
  email: string
  password: string
  confirmPassword: string
  firstName: string
  lastName: string
}

const RegisterForm = ({ loading, setFormData }: any) => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<Props>({})

  const password = useRef({})
  // password.current(watch('password', ''))
  const [passwordVisible, setPasswordVisible] = useState<boolean>(false)
  const [confirmPasswordVisible, setConfirmPasswordVisible] =
    useState<boolean>(false)

  const onSubmit = (data: any) => {
    setFormData(data)
  }

  return (
    <>
      <Form>
        <Title fontSize={[4, 5, 6]}>Register</Title>
        <Form.Base onSubmit={handleSubmit(onSubmit)}>
          <Input
            {...register('email', {
              required: 'This field is required',
              pattern: {
                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                message: 'invalid email address',
              },
            })}
            type="text"
            placeholder="Email"
          />
          {errors.email ? (
            <Error>{errors.email.message}</Error>
          ) : (
            <Error>*</Error>
          )}
          <PasswordContainer>
            <Input
              {...register('password', {
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
              })}
              type={passwordVisible ? 'text' : 'password'}
              placeholder="Password"
            />
            <SeenIcon
              src={
                passwordVisible
                  ? './assets/icons/eyeclosed.png'
                  : './assets/icons/eyeopen.png'
              }
              onClick={() => setPasswordVisible(!passwordVisible)}
            />
          </PasswordContainer>
          {errors.password && (
            <Form.Error>{errors.password.message}</Form.Error>
          )}
          <PasswordContainer>
            <Input
              {...register('confirmPassword', {
                required: 'This field is required',
                validate: (value) =>
                  value === watch('password') || 'Passwords must match',
              })}
              type={passwordVisible ? 'text' : 'password'}
              placeholder="Confirm password"
            />
            <SeenIcon
              src={
                passwordVisible
                  ? './assets/icons/eyeclosed.png'
                  : './assets/icons/eyeopen.png'
              }
              onClick={() => setPasswordVisible(!confirmPasswordVisible)}
            />
          </PasswordContainer>
          {errors.confirmPassword && (
            <Form.Error>{errors.confirmPassword.message}</Form.Error>
          )}
          <Input
            {...register('firstName', { required: 'This field is required' })}
            type="text"
            placeholder="First name"
          />
          {errors.firstName && (
            <Form.Error>{errors.firstName.message}</Form.Error>
          )}
          <Input
            {...register('lastName', { required: 'This field is required' })}
            type="text"
            placeholder="Last name"
          />
          {errors.lastName && (
            <Form.Error>{errors.lastName.message}</Form.Error>
          )}
          <Form.Submit loading={loading}>Sign up</Form.Submit>
        </Form.Base>
      </Form>
    </>
  )
}

export default RegisterForm
