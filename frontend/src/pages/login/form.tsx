import React from 'react';
import Form from '../../components/form';
import {
  Title,
  AccentText,
  Link,
  PlainText,
} from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import FormValidation from './formValidation';

const LoginForm = () => (
  <Form>
    <Title fontSize={[4, 5, 6]} my="10px">
      Log in
    </Title>
    <FormValidation />
    <PlainText>
      {`New to the shelf?  `}
      <Link to={Routes.REGISTER}>Sign up</Link>
    </PlainText>
    <PlainText>
      {`Forgot password?  `}
      <Link to={Routes.FORGOT_PASSWORD}>Reset password</Link>
    </PlainText>
  </Form>
);

export default LoginForm;
