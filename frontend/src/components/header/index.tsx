import {Container,Logo ,Profile} from "./header-styles"

 const Header = ({ profile,...restProps}:any) => {
    return (
       <Container {...restProps}>
          <Logo src="./assets/images/logo.png"/>
          <Profile theme={profile} >PP</Profile>
          </Container>
    )
}





export default Header