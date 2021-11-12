import React, { useState } from 'react'
import { Form, Header, Frame } from '../components'

const RegisterContainer = () => {
  const [showPassword, setShowPassword] = useState(false)
  const [email, setEmail] = useState<string>('')
  const [password, setPassword] = useState<string>('')
  const [firstName, setFirstName] = useState<string>('')
  const [lastName, setLastName] = useState<string>('')

  return (
    <>
      <Header profile={false} />
      <Frame>
        <Frame.ContainerRight>
          <Form>
            <Form.Title>Sign Up</Form.Title>
            <Form.Base>
              <Form.Input 
                type="email"
                placeholder="email address"
                value={password}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => setPassword(event.target.value)}
                />
              <Form.InputControl type="password" placeholder="password" />
              <Form.InputControl
                type="password"
                placeholder="confirm password"
              />
              <Form.Input type="text" placeholder="First Name" />
              <Form.Input type="text" placeholder="Last Name" />

              <Form.Submit>Sign</Form.Submit>
            </Form.Base>
            <Form.AccentText>
              Have an account?
              <Form.Link to="/login">Sign in</Form.Link>
            </Form.AccentText>
          </Form>
        </Frame.ContainerRight>
        <Frame.ContainerLeft>
          <Frame.Feature>
            <Frame.Title>Experience a new world</Frame.Title>
            <Frame.SubTitle>
              Enter your personal details and start your journey with us
            </Frame.SubTitle>
          </Frame.Feature>
        </Frame.ContainerLeft>
      </Frame>
    </>
  )
}

export default RegisterContainer
