import React from 'react';
import { FormContainer } from '../../components/form/form-styles';
import { H2, PlainText, Link } from '../../components/text/text-styles';
import { Routes } from '../../utils/enums/routes';
import FormValidation from './formValidation';

const RegisterForm = () => (
  <FormContainer>
    <H2>Register</H2>
    <FormValidation />
    <PlainText>
      {`Already have an account?  `}
      <Link to={Routes.LOGIN}>Sign in</Link>
    </PlainText>
  </FormContainer>
);

export default RegisterForm;
