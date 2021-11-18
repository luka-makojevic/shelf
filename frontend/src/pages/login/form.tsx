import React, { useContext, useState } from 'react'
import { Form } from '../../components'
import { Title, AccentText, Link } from '../../components/text/text-styles'
import { AuthContext } from '../../providers/authProvider'
import { Routes } from '../../enums/routes'

const LoginForm = () => {
  const [email, setEmail] = useState<string>('')
  const [password, setPassword] = useState<string>('')
  const [passwordVisible, setPasswordVisible] = useState<boolean>(false)
  const [error, setError] = useState<string>('')

  const { login, loading, user } = useContext(AuthContext)
  console.log(user)
  const data = { email, password }

  const emailChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
    setEmail(event.currentTarget.value)
  }

  const handlePasswordChange = (event: React.FormEvent<HTMLInputElement>) => {
    setPassword(event.currentTarget.value)
  }
  const handlePassVisible = () => {
    setPasswordVisible(!passwordVisible)
  }
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    login(
      data,
      () => {},
      (err: any) => setError(err.message)
    )
  }
  return (
    <>
      <Form>
        <Title fontSize={[4, 5, 6]}>Login</Title>
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
          <Form.Submit loading={loading}>Sign in</Form.Submit>
        </Form.Base>
        <AccentText>
          New to shelf?
          <Link to={Routes.REGISTER}> Register</Link>
        </AccentText>
      </Form>
    </>
  )
}

export default LoginForm
