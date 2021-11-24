import React from 'react';
import Form from '../../components/form';
import { Title } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import FormValidation from './formValidation';

const RegisterForm = () => (
  <Form>
    <Title fontSize={[4, 5, 6]}>Register</Title>
    <FormValidation />
    <Form.AccentText>
      {`Already have an account?  `}
      <Form.Link to={Routes.LOGIN}>Sign in</Form.Link>
    </Form.AccentText>
  </Form>
);

export default RegisterForm;
