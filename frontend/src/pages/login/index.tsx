import Header from '../../components/header';
import {
  Wrapper,
  Container,
  Feature,
} from '../../components/layout/layout.styles';
import { H1, Description } from '../../components/text/text-styles';
import LoginForm from './form';

const Login = () => (
  <>
    <Header position="absolute" hideProfile />
    <Wrapper>
      <Container>
        <LoginForm />
      </Container>
      <Container background="primary" isHiddenOnSmallScreen>
        <Feature>
          <H1> Welcome back</H1>
          <Description>
            To keep connected with us, please sign in with your personal info
          </Description>
        </Feature>
      </Container>
    </Wrapper>
  </>
);

export default Login;
