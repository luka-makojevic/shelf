import React from 'react'
import Header from '../../components/header'
import {
  Wrapper,
  Container,
  Feature,
} from '../../components/layout/layout.styles'
import { Title, SubTitle } from '../../components/text/text-styles'
import LoginForm from './form'

const Login = () => (
  <>
    <Header hideProfile />
    <Wrapper
      flexDirection={['column', 'row']}
      alignItems="center"
      justifyContent="center"
    >
      <Container width={[1, 1 / 2]}>
        <LoginForm />
      </Container>
      <Container bg="primary" width={[1, 1 / 2]} display={['none', 'flex']}>
        <Feature height={['550', '600']}>
          <Title fontSize={['40px', '50px']}> Welcome back</Title>
          <SubTitle fontSize={['15px', '19px']}>
            To keep connected with us, please sign in with your personal info
          </SubTitle>
        </Feature>
      </Container>
    </Wrapper>
  </>
)

export default Login
