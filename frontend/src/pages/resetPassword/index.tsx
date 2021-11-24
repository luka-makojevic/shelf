import { Form, Header } from '../../components';
import { Container, Wrapper } from '../../components/layout/layout.styles';
import { Title } from '../../components/text/text-styles';
import ResetPasswordForm from './formValidation';

const ResetPassword = () => (
  <>
    <Header hideProfile />
    <Wrapper
      flexDirection={['column', 'row']}
      alignItems="center"
      justifyContent="center"
    >
      <Container width={[1, 1 / 2]}>
        <Form>
          <Title fontSize={[4, 5, 6]}>Reset Password</Title>
          <ResetPasswordForm />
        </Form>
      </Container>
    </Wrapper>
  </>
);

export default ResetPassword;
