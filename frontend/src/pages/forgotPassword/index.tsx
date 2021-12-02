import { Form, Header } from '../../components';
import { Container, Wrapper } from '../../components/layout/layout.styles';
import FormValidation from './formValidation';
import { Title } from '../../components/text/text-styles';

const ForgotPassword = () => (
  <>
    <Header hideProfile />
    <Wrapper
      flexDirection={['column', 'row']}
      alignItems="center"
      justifyContent="center"
    >
      <Container width={[1, 1 / 2]}>
        <Form>
          <Title fontSize={[4, 5, 6]}>Send email</Title>
          <FormValidation />
        </Form>
      </Container>
    </Wrapper>
  </>
);

export default ForgotPassword;
