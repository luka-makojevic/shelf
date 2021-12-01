import React from 'react';
import Form from '../../components/form';
import { Title, PlainText, Link } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import FormValidation from './formValidation';

const RegisterForm = () => (
  <Form>
    <Title fontSize={[4, 5, 6]} my="10px">
      Register
    </Title>
    <FormValidation />
    <PlainText>
      {`Already have an account?  `}
      <Link to={Routes.LOGIN}>Sign in</Link>
    </PlainText>
  </Form>
);

export default RegisterForm;
