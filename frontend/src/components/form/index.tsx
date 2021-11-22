import React, { useState } from 'react'
import {
  Container,
  Base,
  Title,
  Submit,
  AccentText,
  Link,
  Spinner,
} from './form-styles'

const Form = ({ children, ...restProps }: any) => (
  <Container width={['300px', '400px']} height={['550', '600']} {...restProps}>
    {children}
  </Container>
)
export default Form

Form.Base = function FormBase({ children, ...restProps }: any) {
  return <Base {...restProps}>{children}</Base>
}

Form.Title = function FormTitle({ children, ...restProps }: any) {
  return <Title {...restProps}>{children}</Title>
}

Form.Spinner = function FormSpinner({ ...restProps }: any) {
  return <Spinner {...restProps} />
}

Form.Submit = function FormSubmit({ loading, children }: any) {
  return (
    <Submit>
      {loading ? (
        <Spinner src="./assets/icons/loading.png" alt="loading" />
      ) : (
        children
      )}
    </Submit>
  )
}

Form.AccentText = function FormAccentText({ children, ...restProps }: any) {
  return <AccentText {...restProps}>{children}</AccentText>
}

Form.Link = function FormLink({ children, ...restProps }: any) {
  return <Link {...restProps}>{children}</Link>
}
