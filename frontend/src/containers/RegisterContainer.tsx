import React ,{useState} from 'react'
import { Form, Header, Frame } from '../components'
import { useAuthService } from '../hooks/useAuth'

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

  const handleSubmit = (e: React.SyntheticEvent) => {
    e.preventDefault()
    if(email && password && password&& confirmPassword && firstName && lastName )
    setLoading(true)
    register(email, password, firstName, lastName)
      .then((data: any) => {
        setLoading(false)
        console.log(data)
      })
      .catch((err: any) => {
        setLoading(false)
        setError(err.message)
        console.log(err)
      })
  }

  return (
    <>
      <Header profile={false} />
      <Frame>
        <Frame.ContainerRight>
          <Form>
            <Form.Title>Sign Up</Form.Title>
            <Form.Error>{error}</Form.Error>
            <Form.Base onSubmit={handleSubmit}>
              <Form.Input
                type="email"
                placeholder="email address"
                value={email}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setEmail(event.target.value)
                }
              />
              <Form.InputControl>
                <Form.Input
                  type={showPassword ? 'text' : 'password'}
                  placeholder="password"
                  value={password}
                  onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                    setPassword(event.target.value)
                  }
                  
                />
                <Form.SeenIcon
                  src={
                    showPassword
                      ? './assets/icons/eyeclosed.png'
                      : './assets/icons/eyeopen.png'
                  }
                  onClick={() => setShowPassword(!showPassword)}
                />
              </Form.InputControl>
              <Form.InputControl>
                <Form.Input
                  type={showConfirmPassword ? 'text' : 'password'}
                  placeholder="confirm password"
                  value={confirmPassword}
                  onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                    setConfirmPassword(event.target.value)
                  }
                />
                <Form.SeenIcon
                  src={
                    showConfirmPassword
                      ? './assets/icons/eyeclosed.png'
                      : './assets/icons/eyeopen.png'
                  }
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                />
              </Form.InputControl>

              <Form.Input
                type="text"
                placeholder="First Name"
                value={firstName}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setFirstName(event.target.value)
                }
              />
              <Form.Input
                type="text"
                placeholder="Last Name"
                value={lastName}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setLastName(event.target.value)
                }
              />
              <Form.InputControl>
                <Form.Submit>
                  {!loading ? (
                    'Sign'
                  ) : (
                    <Form.Spinner
                      src="./assets/icons/loading.png"
                      alt="loading"
                    />
                  )}
                </Form.Submit>
              </Form.InputControl>
            </Form.Base>
            <Form.AccentText>
              Have an account?
              <Form.Link to="/login"> Sign in</Form.Link>
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

export { RegisterContainer }
