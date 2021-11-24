import React from 'react';
import {
  Container,
  Base,
  Submit,
  AccentText,
  Link,
  Spinner,
} from './form-styles';

const Form = ({ children }: any) => (
  <Container width={['300px', '400px']} height={['550', '600']}>
    {children}
  </Container>
);

export default Form;

Form.Base = function FormBase({ children, onSubmit }: any) {
  return <Base onSubmit={onSubmit}>{children}</Base>;
};

Form.Spinner = function FormSpinner() {
  return <Spinner />;
};

Form.Submit = function FormSubmit({ isLoading, children, onClick }: any) {
  return (
    <Submit onClick={onClick}>
      {isLoading ? (
        <Spinner src="./assets/icons/loading.png" alt="loading" />
      ) : (
        children
      )}
    </Submit>
  );
};

Form.AccentText = function FormAccentText({ children }: any) {
  return <AccentText>{children}</AccentText>;
};

Form.Link = function FormLink({ children, to }: any) {
  return <Link to={to}>{children}</Link>;
};
