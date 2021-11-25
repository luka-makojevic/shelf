import React from 'react';
import Form from '../../components/form';
import { Title, AccentText, Link } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import FormValidation from './formValidation';

const LoginForm = () => (
  <Form>
    <Title fontSize={[4, 5, 6]}>Log in</Title>
    <FormValidation />
    <AccentText>
      {`New to the shelf?  `}
      <Link to={Routes.REGISTER}>Sign up</Link>
    </AccentText>
  </Form>
);

export default LoginForm;
