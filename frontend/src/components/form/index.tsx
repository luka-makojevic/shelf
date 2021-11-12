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
    InputControl,
    Spinner  
    } from "./form-styles"



 const Form = ({children, ...restProps}:any) => {
    return (
       <Container {...restProps}>{children}</Container>
    )
}

Form.Base =  function FormBase({children, ...restProps}: any) {
    return <Base {...restProps}>{children}</Base>
}

Form.Title = function FormTitle({children, ...restProps}: any) {
    return <Title {...restProps}>{children}</Title>
}

Form.Input =  function FormInput({children, ...restProps}: any) {
    return <Input {...restProps}>{children}</Input>
}

Form.InputControl =  function FormInputControl({children, ...restProps}: any) {
    return <InputControl {...restProps}>{children}</InputControl>
}
Form.Spinner =  function FormSpinner({...restProps}: any) {
    return <Spinner {...restProps}/>
}


Form.Submit = function FormSubmit({ children, ...restProps }: any) {
    return <Submit {...restProps}>{children}</Submit>
}

Form.AccentText = function FormAccentText({children, ...restProps}: any) {
    return <AccentText {...restProps}>{children}</AccentText>
}

Form.Error = function FormError({ children, ...restProps }: any) {
    return <Error {...restProps}>{children}</Error>
}

Form.Link = function FormLink({ children, ...restProps }: any) {
    return <Link {...restProps}>{children}</Link>
}

Form.SeenIcon = function FormSeenIcon({ children, ...restProps }: any) {
    return <SeenIcon {...restProps}>{children}</SeenIcon>
}



















export default Form