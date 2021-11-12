import React, { useState } from 'react'
import Header from '../components/header'
import Form from '../components/form'
import Frame from '../components/authFrame'

const LoginContainer = () => {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const handleChangeEmail = (event: React.FormEvent<HTMLInputElement>) => {
    const newValue = event.currentTarget.value
    setEmail(newValue)
    console.log(email)
  }

  const handleChangePassword = (event: React.FormEvent<HTMLInputElement>) => {
    const newValue = event.currentTarget.value
    setPassword(newValue)
    console.log(password)
  }

  const handleSubmit = (event: React.MouseEvent) => {
    event.preventDefault()
  }

  return (
    <>
      <Header profile={false} />
      <Frame>
        <Frame.ContainerRight>
          <Form>
            <Form.Title>Login</Form.Title>
            <Form.Base>
              <Form.Input
                placeholder="email"
                type="email"
                value={email}
                onChange={handleChangeEmail}
              />
              <Form.Input
                type="password"
                value={password}
                onChange={handleChangePassword}
              />
              <Form.Submit>Sign in</Form.Submit>
            </Form.Base>
            <Form.AccentText>
              New to Shelf?
              <Form.Link to="/register" onClick={handleSubmit}>
                Sign Up
              </Form.Link>
            </Form.AccentText>
          </Form>
        </Frame.ContainerRight>
        <Frame.ContainerLeft>
          <Frame.Feature>
            <Frame.Title>Welcome back</Frame.Title>
            <Frame.SubTitle>
              To keep connected with us, please sign in with your personal info
            </Frame.SubTitle>
          </Frame.Feature>
        </Frame.ContainerLeft>
      </Frame>
    </>
  )
}

export default LoginContainer
