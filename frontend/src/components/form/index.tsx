import React from 'react';
import { Container, Base, Submit } from './form-styles';
import {
  FormProps,
  FormBaseProps,
  FormSubmitProps,
} from '../../interfaces/types';

const Form = ({ children }: FormProps) => (
  <Container width={['300px', '400px']} height={['550', '600']}>
    {children}
  </Container>
);

export default Form;

Form.Base = function FormBase({ children, onSubmit }: FormBaseProps) {
  return <Base onSubmit={onSubmit}>{children}</Base>;
};


