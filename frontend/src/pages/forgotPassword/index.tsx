import { Header } from '../../components';
import { Container, Wrapper } from '../../components/layout/layout.styles';
import FormValidation from './formValidation';
import { H2 } from '../../components/text/text-styles';
import { FormContainer } from '../../components/form/form-styles';

const ForgotPassword = () => (
  <>
    <Header hideProfile position="absolute" />
    <Wrapper>
      <Container>
        <FormContainer>
          <H2>Send email</H2>
          <FormValidation />
        </FormContainer>
      </Container>
    </Wrapper>
  </>
);

export default ForgotPassword;
