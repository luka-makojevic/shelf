import React, { useContext, useState } from 'react'
import { Form } from '../../components'
import { Title } from '../../components/text/text-styles'
import { AuthContext } from '../../providers/authProvider'

const RegisterForm = () => {
  const [email, setEmail] = useState<string>('')
  const [password, setPassword] = useState<string>('')
  const [confirmPassword, setConfirmPassword] = useState<string>('')
  const [firstName, setFirstName] = useState<string>('')
  const [lastName, setLastName] = useState<string>('')
  const [passwordVisible, setPasswordVisible] = useState<boolean>(false)
  const [confirmPasswordVisible, setConfirmPasswordVisible] =
    useState<boolean>(false)
  const [error, setError] = useState<string>()

  const { register, loading } = useContext(AuthContext)
  const data = { email, password, firstName, lastName }

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

  const handlePasswordChange = (event: React.FormEvent<HTMLInputElement>) => {
    setPassword(event.currentTarget.value)
    console.log(event.currentTarget.value)
  }

  const handleConfirmPasswordChange = (
    event: React.FormEvent<HTMLInputElement>
  ) => {
    setConfirmPassword(event.currentTarget.value)
    console.log(event.currentTarget.value)
  }
  const handlePassVisible = (event: React.MouseEvent) => {
    setPasswordVisible(!passwordVisible)
  }
  const handleConfPassVisible = (event: React.MouseEvent) => {
    setConfirmPasswordVisible(!confirmPasswordVisible)
  }
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    register(
      data,
      () => {},
      (err: any) => setError(err.message)
    )
  }
  return (
    <>
      <Form>
        <Title fontSize={[4, 5, 6]}>Register</Title>
        <Form.Error>{error}</Form.Error>
        <Form.Base onSubmit={handleSubmit}>
          <Form.Input
            type="email"
            placeholder="Email"
            value={email}
            handleInputChange={emailChangeHandler}
          />
          <Form.InputPassword
            placeholder="Password"
            value={password}
            onChange={handlePasswordChange}
            passwordVisible={passwordVisible}
            onClick={handlePassVisible}
          />
          <Form.InputPassword
            placeholder="Confirm password"
            value={confirmPassword}
            onChange={handleConfirmPasswordChange}
            passwordVisible={confirmPasswordVisible}
            onClick={handleConfPassVisible}
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
          <Form.Submit loading={loading}>Sign up</Form.Submit>
        </Form.Base>
      </Form>
    </>
  )
}

export default RegisterForm
