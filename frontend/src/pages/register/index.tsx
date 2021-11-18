import React, { useState } from 'react'
import { Header } from '../../components'
import {
  Wrapper,
  Container,
  Feature,
} from '../../components/layout/layout.styles'
import { Title, SubTitle } from '../../components/text/text-styles'
import RegisterForm from './form'

const Register = () => {
  const [error, setError] = useState<string>('')
  const [loading, setLoading] = useState(false)
  const [formData, setFormData] = useState()

  console.log('formData - index', formData)

  const handleSubmit = () => {
    // poziva register
  }

  return (
    <>
      <Header hideProfile={true} />
      <Wrapper flexDirection={['column', 'row']} height="100vh">
        <Container width={[1, 1 / 2]}>
          <RegisterForm loading={loading} setFormData={setFormData} />
        </Container>
        <Container bg="primary" width={[1, 1 / 2]} display={['none', 'flex']}>
          <Feature height={['550', '600']}>
            <Title fontSize={['40px', '50px']}>Explore new world</Title>
            <SubTitle fontSize={['15px', '19px']}>
              Enter your personal details and start your journey with us
            </SubTitle>
          </Feature>
        </Container>
      </Wrapper>
    </>
  )
}

export { Register }
