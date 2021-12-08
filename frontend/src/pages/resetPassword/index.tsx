import { Header } from '../../components';
import { FormContainer } from '../../components/form/form-styles';
import { Container, Wrapper } from '../../components/layout/layout.styles';
import { H2 } from '../../components/text/text-styles';
import ResetPasswordForm from './formValidation';

const ResetPassword = () => (
  <>
    <Header hideProfile position="absolute" />
    <Wrapper>
      <Container>
        <FormContainer>
          <H2>Reset Password</H2>
          <ResetPasswordForm />
        </FormContainer>
      </Container>
    </Wrapper>
  </>
);

export default ResetPassword;
