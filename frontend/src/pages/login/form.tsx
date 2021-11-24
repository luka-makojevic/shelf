import React from 'react';
import Form from '../../components/form';
import { Title } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import FormValidation from './formValidation';

const LoginForm = () => (
  <Form>
    <Title fontSize={[4, 5, 6]}>Log in</Title>
    <FormValidation />
    <Form.AccentText>
      {`New to the shelf?  `}
      <Form.Link to={Routes.REGISTER}>Sign in</Form.Link>
    </Form.AccentText>
    <Form.AccentText>
      {`Forgot password?  `}
      <Form.Link to={Routes.FORGOT_PASSWORD}>Reset password</Form.Link>
    </Form.AccentText>
  </Form>
);

export default LoginForm;
