import React from 'react';
import Form from '../../components/form';
import { Title, AccentText, Link } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import FormValidation from './formValidation';

const RegisterForm = () => (
  <Form>
    <Title fontSize={[4, 5, 6]}>Register</Title>
    <FormValidation />
    <AccentText>
      {`Already have an account?  `}
      <Link to={Routes.LOGIN}>Sign in</Link>
    </AccentText>
  </Form>
);

export default RegisterForm;
