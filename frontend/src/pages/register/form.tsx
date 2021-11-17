import React, { useState } from 'react'
import { Form } from '../../components'
import { Title } from '../../components/text/text-styles'

const RegisterForm = () => {
  const [email, setEmail] = useState<string>('')
  const [password, setPassword] = useState<string>('')
  const [confirmPassword, setConfirmPassword] = useState<string>('')
  const [firstName, setFirstName] = useState<string>('')
  const [lastName, setLastName] = useState<string>('')

  const emailChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
    setEmail(event.currentTarget.value)
    console.log(event.currentTarget.value)
  }

  const firstNameChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
    setFirstName(event.currentTarget.value)
    console.log(event.currentTarget.value)
  }

  const lastNameChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
    setLastName(event.currentTarget.value)
    console.log(event.currentTarget.value)
  }

  const passwordChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
    setPassword(event.currentTarget.value)
    console.log(event.currentTarget.value)
  }

  const confirmPasswordChangeHandler = (
    event: React.FormEvent<HTMLInputElement>
  ) => {
    setConfirmPassword(event.currentTarget.value)
    console.log(event.currentTarget.value)
  }

  return (
    <>
      <Form>
        <Title fontSize={[4, 5, 6]}>Register</Title>
        <Form.Error>Error</Form.Error>
        <Form.Base>
          <Form.Input
            type="email"
            placeholder="Email"
            value={email}
            handleInputChange={emailChangeHandler}
          />
          <Form.InputPassword
            placeholder="Password"
            value={password}
            setPassword={passwordChangeHandler}
          />
          <Form.InputPassword
            placeholder="Confirm password"
            value={confirmPassword}
            setPassword={confirmPasswordChangeHandler}
          />
          <Form.Input
            type="email"
            placeholder="First name"
            value={firstName}
            handleInputChange={firstNameChangeHandler}
          />
          <Form.Input
            type="email"
            placeholder="Last name"
            value={lastName}
            handleInputChange={lastNameChangeHandler}
          />
          <Form.Submit>Sign up</Form.Submit>
        </Form.Base>
      </Form>
    </>
  )
}

export default RegisterForm
