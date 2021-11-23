import React from 'react';
import Form from '../../components/form';
import { Title } from '../../components/text/text-styles';
import FormValidation from './formValidation';

const RegisterForm = () => (
  <Form>
    <Title fontSize={[4, 5, 6]}>Register</Title>
    <FormValidation />
    <Form.AccentText>
      {`Already have an account?  `}
      <Form.Link to="/signin">Sign in</Form.Link>
    </Form.AccentText>
  </Form>
);

export default RegisterForm;
