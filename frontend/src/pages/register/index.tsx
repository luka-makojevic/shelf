import React from 'react';
import { Header } from '../../components';
import {
  Wrapper,
  Container,
  Feature,
} from '../../components/layout/layout.styles';
import { H1, Description } from '../../components/text/text-styles';
import RegisterForm from './form';

const Register = () => (
  <>
    <Header hideProfile position="absolute" />
    <Wrapper>
      <Container>
        <RegisterForm />
      </Container>
      <Container background="primary" isHiddenOnSmallScreen>
        <Feature>
          <H1>Explore a new world</H1>
          <Description>
            Enter your personal details and start your journey with us :)
          </Description>
        </Feature>
      </Container>
    </Wrapper>
  </>
);

export default Register;
