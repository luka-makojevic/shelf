import React from 'react';
import { FormContainer } from '../../components/form/form-styles';
import { H2, Link, PlainText } from '../../components/text/text-styles';
import { Routes } from '../../utils/enums/routes';
import FormValidation from './formValidation';

const LoginForm = () => (
  <FormContainer>
    <H2>Log in</H2>
    <FormValidation />
    <PlainText>
      {`New to the shelf?  `}
      <Link to={Routes.REGISTER}>Sign up</Link>
    </PlainText>
    <PlainText>
      {`Forgot password?  `}
      <Link to={Routes.FORGOT_PASSWORD}>Reset password</Link>
    </PlainText>
  </FormContainer>
);

export default LoginForm;
