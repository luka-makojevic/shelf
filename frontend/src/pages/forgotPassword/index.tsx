import { Header } from '../../components';
import { Container, Wrapper } from '../../components/layout/layout.styles';
import ForgotPasswordForm from './form';

const ForgotPassword = () => (
  <>
    <Header hideProfile position="absolute" />
    <Wrapper>
      <Container>
        <ForgotPasswordForm />
      </Container>
    </Wrapper>
  </>
);

export default ForgotPassword;
