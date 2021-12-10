import { Header } from '../../components';
import { Container, Wrapper } from '../../components/layout/layout.styles';
import ResetPasswordForm from './form';

const ResetPassword = () => (
  <>
    <Header hideProfile position="absolute" />
    <Wrapper>
      <Container>
        <ResetPasswordForm />
      </Container>
    </Wrapper>
  </>
);

export default ResetPassword;
