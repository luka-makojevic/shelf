import React, { useState } from 'react'
import { Form, Header } from '../components'
import { useAuthService } from '../hooks/useAuth'
import {
  SubTitle,
  Title,
  Inner,
  Container,
  Feature,
} from '../components/layout/layout.styles'

const RegisterContainer = () => {
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [email, setEmail] = useState<string>('')
  const [password, setPassword] = useState<string>('')
  const [confirmPassword, setConfirmPassword] = useState<string>('')
  const [firstName, setFirstName] = useState<string>('')
  const [lastName, setLastName] = useState<string>('')
  const [error, setError] = useState()
  const [loading, setLoading] = useState(false)

  const { register } = useAuthService()
  const data = { password, email, firstName, lastName }

  const handleSubmit = (e: React.SyntheticEvent) => {
    e.preventDefault()
    if (!email && !password && !confirmPassword && !firstName && !lastName)
      return
    setLoading(true)
    register(data).catch((err) => {
      setLoading(false)
      console.log(err.message)
    })
  }

  return (
    <>
      <Header hideProfile={true} />
      <Inner flexDirection={['column', 'row']}>
        <Container width={['100%', '50%']}>
          <Form>
            <Form.Title>Sign Up</Form.Title>
            <Form.Error>{error}</Form.Error>
            <Form.Base onSubmit={handleSubmit}>
              <Form.Input
                type="email"
                placeholder="email address"
                value={email}
                setInput={setEmail}
              />
              <Form.InputPassword
                setPasswordVisible={setShowPassword}
                passwordVisible={showPassword}
                value={password}
                setPassword={setPassword}
                placeholder="password"
              />

              <Form.InputPassword
                setPasswordVisible={setShowConfirmPassword}
                passwordVisible={showConfirmPassword}
                value={confirmPassword}
                setPassword={setConfirmPassword}
                placeholder="confirm password"
              />

              <Form.Input
                type="text"
                placeholder="First Name"
                value={firstName}
                setInput={setFirstName}
              />
              <Form.Input
                type="text"
                placeholder="First Name"
                value={lastName}
                setInput={setLastName}
              />

              <Form.Submit loading={loading} />
            </Form.Base>
            <Form.AccentText>
              Have an account?
              <Form.Link to="/login"> Sign in</Form.Link>
            </Form.AccentText>
          </Form>
        </Container>
        <Container bg="primary">
          <div>Hello</div>
        </Container>
      </Inner>
    </>
  )
}

export { RegisterContainer }
