import {
  Container,
  Base,
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
    <Container
      width={['300px', '400px']}
      height={['550', '600']}
      {...restProps}
    >
      {children}
    </Container>
  )
}

Form.Base = function FormBase({ children, ...restProps }: any) {
  return <Base {...restProps}>{children}</Base>
}

Form.Input = function FormInput({ handleInputChange, ...restProps }: any) {
  return (
    <Input
      marginY={[1, 2]}
      padding={[1, 2]}
      {...restProps}
      onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
        handleInputChange(event)
      }}
    />
  )
}

Form.InputPassword = function FormInputPassword({
  onClick,
  passwordVisible,
  value,
  onChange,
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
        onChange={onChange}
      />
      <SeenIcon
        src={
          passwordVisible
            ? './assets/icons/eyeopen.png'
            : './assets/icons/eyeclosed.png'
        }
        onClick={onClick}
      />
    </PasswordContainer>
  )
}
Form.Spinner = function FormSpinner({ ...restProps }: any) {
  return <Spinner {...restProps} />
}

Form.Submit = function FormSubmit({ loading, children }: any) {
  return (
    <Submit>
      {!loading ? (
        children
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
