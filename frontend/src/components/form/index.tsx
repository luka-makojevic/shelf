import {
  Container,
  Base,
  Title,
  Input,
  Submit,
  AccentText,
  Error,
  Link,
  SeenIcon,
  InputPassword,
  Spinner,
  PasswordContainer,
} from './form-styles'

const Form = ({ children, ...restProps }: any) => {
  return (
    <Container width={['300px', '400px']} padding={[3, 4, 5]} {...restProps}>
      {children}
    </Container>
  )
}

Form.Base = function FormBase({ children, ...restProps }: any) {
  return <Base {...restProps}>{children}</Base>
}

Form.Title = function FormTitle({ children, ...restProps }: any) {
  return <Title {...restProps}>{children}</Title>
}

Form.Input = function FormInput({ setInput, ...restProps }: any) {
  return (
    <Input
      marginY={[1, 2]}
      padding={[1, 2]}
      {...restProps}
      onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
        setInput(event.target.value)
      }
    />
  )
}

Form.InputPassword = function FormInputPassword({
  setPasswordVisible,
  passwordVisible,
  value,
  setPassword,
  placeholder,
  ...restProps
}: any) {
  return (
    <PasswordContainer>
      <Input
        marginY={[1, 2]}
        padding={[1, 2]}
        type={passwordVisible ? 'text' : 'password'}
        placeholder={placeholder}
        value={value}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
          setPassword(event.target.value)
        }
      />
      <SeenIcon
        src={
          passwordVisible
            ? './assets/icons/eyeclosed.png'
            : './assets/icons/eyeopen.png'
        }
        onClick={() => setPasswordVisible(!passwordVisible)}
      />
    </PasswordContainer>
  )
}
Form.Spinner = function FormSpinner({ ...restProps }: any) {
  return <Spinner {...restProps} />
}

Form.Submit = function FormSubmit({ loading }: any) {
  return (
    <Submit>
      {!loading ? (
        'Sign'
      ) : (
        <Spinner src="./assets/icons/loading.png" alt="loading" />
      )}
    </Submit>
  )
}

Form.AccentText = function FormAccentText({ children, ...restProps }: any) {
  return <AccentText {...restProps}>{children}</AccentText>
}

Form.Error = function FormError({ children, ...restProps }: any) {
  return <Error {...restProps}>{children}</Error>
}

Form.Link = function FormLink({ children, ...restProps }: any) {
  return <Link {...restProps}>{children}</Link>
}

Form.SeenIcon = function FormSeenIcon({
  setPasswordVisible,
  passwordVisible,
  ...restProps
}: any) {
  return (
    <SeenIcon
      src={
        passwordVisible
          ? './assets/icons/eyeclosed.png'
          : './assets/icons/eyeopen.png'
      }
      onClick={() => setPasswordVisible(!passwordVisible)}
      {...restProps}
    />
  )
}

export default Form
